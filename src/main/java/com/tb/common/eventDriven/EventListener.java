package com.tb.common.eventDriven;

public interface EventListener {
    // Called when a response is received for a tracked request
    void onResponseReceived(ExpirableEvent event);

    // Called when an event expires without receiving a response
    void onEventExpired(ExpirableEvent event);
}
