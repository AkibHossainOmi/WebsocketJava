package com.tb.common.eventDriven;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KeepAliveTracker {
    private final EventStore eventStore;
    private final ScheduledExecutorService scheduler;
    private final int monitoringIntervalInSeconds;
    private final int consecutiveExpiryThreshold;
    private final int consecutiveResponseThreshold;
    private int consecutiveExpiryCount = 0; // Count for consecutive expirations
    private int consecutiveResponseCount = 0; // Count for consecutive responses
    private boolean isUpState = false; // Track whether in "up" state
    private boolean isDownState = false; // Track whether in "down" state
    private final List<EventListener> listeners = new ArrayList<>(); // Listeners for state changes
    public KeepAliveTracker(int maxEventsToStore, boolean throwIfDuplicateEvent,
                            int monitoringIntervalInSeconds,
                            int consecutiveExpiryThreshold,
                            int consecutiveResponseThreshold) {
        this.eventStore = new EventStore(maxEventsToStore, throwIfDuplicateEvent);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.monitoringIntervalInSeconds = monitoringIntervalInSeconds;
        this.consecutiveExpiryThreshold = consecutiveExpiryThreshold;
        this.consecutiveResponseThreshold = consecutiveResponseThreshold;
        // Schedule periodic monitoring
        startMonitoring();
    }


    private void startMonitoring() {
        scheduler.scheduleAtFixedRate(this::checkForExpiredRequests,
                monitoringIntervalInSeconds, monitoringIntervalInSeconds, TimeUnit.SECONDS);
    }

    private void checkForExpiredRequests() {
        // Logic to check and handle expired requests
        for (String requestId : eventStore.getCurrentEventCount()) {
            ExpirableEvent<?> event = eventStore.getEvent(requestId);
            if (event != null && event.isExpired()) {
                handleEventExpired(event);
            }
        }
    }

    private void handleEventExpired(ExpirableEvent<?> event) {
        // Ignore expirations if already in "down" state
        if (isDownState) {
            return;
        }

        consecutiveExpiryCount++;
        consecutiveResponseCount = 0; // Reset response count

        if (consecutiveExpiryCount >= consecutiveExpiryThreshold) {
            isDownState = true; // Mark as down state
            isUpState = false; // Ensure up state is reset
            notifyListeners("down");
        }
    }

    private void handleResponseReceived(ExpirableEvent<?> event) {
        // Ignore responses if already in "up" state
        if (isUpState) {
            return;
        }

        consecutiveResponseCount++;
        consecutiveExpiryCount = 0; // Reset expiry count

        if (consecutiveResponseCount >= consecutiveResponseThreshold) {
            isUpState = true; // Mark as up state
            isDownState = false; // Ensure down state is reset
            notifyListeners("up");
        }
    }

    private void notifyListeners(String status) {
        for (KeepAliveListener listener : listeners) {
            listener.onKeepAliveStatusChange(status);
        }
    }

    public void addListener(KeepAliveListener listener) {
        listeners.add(listener);
    }

    public void removeListener(KeepAliveListener listener) {
        listeners.remove(listener);
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt(); // Restore interrupt status
        }
    }
}
