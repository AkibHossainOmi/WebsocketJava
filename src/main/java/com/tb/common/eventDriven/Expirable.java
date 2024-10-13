package com.tb.common.eventDriven;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Expirable implements Event {
    @Override
    public String getId() {
        return id;
    }

    private String id;
    private Payload request;
    private Payload response;
    private final LocalDateTime eventTime; // Time when the event was created
    private final LocalDateTime expiredOn; // Expiration time
    private final AtomicBoolean expired = new AtomicBoolean(false); // Flag to indicate if the event is expired
    private final List<RequestStatusListener> listeners = new ArrayList<>(); // List of listeners
    private final ScheduledExecutorService expirationScheduler;
    public Expirable(String id, int expirationInSeconds, Payload request) {
        this.id=id;
        this.eventTime = LocalDateTime.now();
        this.request=request;
        this.expiredOn = this.eventTime.plusSeconds(expirationInSeconds);
        this.listeners.addAll(listeners);
        this.expirationScheduler = Executors.newSingleThreadScheduledExecutor();
        expirationScheduler.schedule(() -> {
            this.expired.set(true);
            this.notifyEventExpiry(); // Notify all listeners that the event has expired
        }, expirationInSeconds, TimeUnit.SECONDS);
    }
    public void setResponse(Payload response) {
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
    public void addListener(RequestStatusListener listener) {
        listeners.add(listener);
    }
    public void removeListener(RequestStatusListener listener) {
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
        for (RequestStatusListener listener : listeners) {
            listener.onEventExpired(this);
        }
    }
    private void notifyResponseReceived() {
        for (RequestStatusListener listener : listeners) {
            listener.onResponseReceived(this);
        }
    }
}
