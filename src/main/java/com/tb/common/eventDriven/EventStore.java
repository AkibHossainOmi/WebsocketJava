package com.tb.common.eventDriven;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;

public class EventStore {
    private final ConcurrentHashMap<String, ExpirableEvent> eventTable = new ConcurrentHashMap<>();
    private final boolean throwIfDuplicateEvent;
    private final int maxEventsToStore;
    public EventStore(int maxEventsToStore, boolean throwIfDuplicateEvent) {
        this.throwIfDuplicateEvent = throwIfDuplicateEvent;
        this.maxEventsToStore = maxEventsToStore;
    }
    public void add(ExpirableEvent event) {
        String requestId = event.getId();
        if (eventTable.containsKey(requestId)) {
            if (this.throwIfDuplicateEvent) {
                throw new RuntimeException("Duplicate eventId: " + requestId);
            }
            // If not throwing an exception, simply return without storing
            return;
        }
        // Check if we need to remove the oldest event
        if (eventTable.size() >= maxEventsToStore) {
            removeOldestEvent();
        }
        eventTable.put(requestId, event);
    }
    private void removeOldestEvent() {
        Iterator<String> iterator = eventTable.keys().asIterator();
        if (iterator.hasNext()) {
            String oldestKey = iterator.next();
            eventTable.remove(oldestKey);
        }
    }
    // Optional: Add methods for retrieving or checking events
    public ExpirableEvent getEvent(String requestId) {
        return eventTable.get(requestId);
    }
    public boolean containsEvent(String requestId) {
        return eventTable.containsKey(requestId);
    }
    public int getCurrentEventCount() {
        return eventTable.size();
    }
}

