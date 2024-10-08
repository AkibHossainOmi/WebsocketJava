package com.tb.common.eventDriven;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ExpirableEvent<TResp> implements Event {
    private String id;
    private TResp response;
    private final LocalDateTime eventTime; // Time when the event was created
    private final LocalDateTime expiredOn; // Expiration time
    private final AtomicBoolean expired = new AtomicBoolean(false); // Flag to indicate if the event is expired
    private final List<EventListener> listeners = new ArrayList<>(); // List of listeners
    private final ScheduledExecutorService expirationScheduler;
    public abstract String getId();
    public ExpirableEvent(String id,int expirationInSeconds) {
        this.id=id;
        this.eventTime = LocalDateTime.now();
        this.expiredOn = this.eventTime.plusSeconds(expirationInSeconds);
        this.listeners.addAll(listeners);
        this.expirationScheduler = Executors.newSingleThreadScheduledExecutor();
        expirationScheduler.schedule(() -> {
            expired.set(true);
            notifyEventExpiry(); // Notify all listeners that the event has expired
        }, expirationInSeconds, TimeUnit.SECONDS);
    }
    public void setResponse(TResp response) {
        this.response = response;
        expirationScheduler.shutdown(); // Cancel timer upon receiving response
    }
    public LocalDateTime getEventTime() {
        return eventTime;
    }
    public LocalDateTime getExpiredOn() {
        return expiredOn;
    }
    public boolean isResponseReceived() {
        return this.response != null;
    }
    public boolean isExpired() {
        return expired.get() || LocalDateTime.now().isAfter(expiredOn);
    }
    public void addListener(EventListener listener) {
        listeners.add(listener);
    }
    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }
    public void cleanup() {
        expirationScheduler.shutdown();
        try {
            if (!expirationScheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                expirationScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            expirationScheduler.shutdownNow();
            Thread.currentThread().interrupt(); // Restore interrupt status
        }
    }
    private void notifyEventExpiry() {
        for (EventListener listener : listeners) {
            listener.onEventExpired(this);
        }
    }
    private void notifyResponseReceived() {
        for (EventListener listener : listeners) {
            listener.onResponseReceived(this);
        }
    }
}
