package com.tb.common.eventDriven;
import com.tb.common.eventDriven.RequestAndResponse.RequestResponse;
import com.tb.common.eventDriven.RequestAndResponse.Payload;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;

public class RequestStore {
    private final ConcurrentHashMap<String, RequestResponse> eventTable = new ConcurrentHashMap<>();
    private final boolean throwIfDuplicateEvent;
    private final int maxEventsToStore;
    private final List<RequestStatusListener> publicListeners = new ArrayList<>();

    public RequestStore(int maxEventsToStore, boolean throwIfDuplicateEvent,
                        List<RequestStatusListener> publicListeners) {
        this.throwIfDuplicateEvent = throwIfDuplicateEvent;
        this.maxEventsToStore = maxEventsToStore;
        for (RequestStatusListener publicListener : publicListeners) {
            this.publicListeners.add(publicListener);
        }
    }
    public void add(Payload request) {
        String requestId = request.getId();
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
        eventTable.put(requestId, new RequestResponse(request));
    }
    public void addResponse(Payload response){
        RequestResponse rr = this.eventTable.get(response.getId());
        if (rr!=null){
            if(rr.responses.stream().count()==0) rr.addResponse(response);
            for (RequestStatusListener publicListener : this.publicListeners) {
                publicListener.onResponseReceived(response);
            }
        }
    }
    private void removeOldestEvent() {
        Iterator<String> iterator = eventTable.keys().asIterator();
        if (iterator.hasNext()) {
            String oldestKey = iterator.next();
            eventTable.remove(oldestKey);
        }
    }
    // Optional: Add methods for retrieving or checking events
    public Payload getRequestById(String requestId) {
        RequestResponse rr= eventTable.get(requestId);
        return rr.request;
    }
    public boolean containsEvent(String requestId) {
        return eventTable.containsKey(requestId);
    }
    public int getCurrentEventCount() {
        return eventTable.size();
    }
    public void addEventStatusListener(RequestStatusListener listener){
        this.publicListeners.add(listener);
    }
}

