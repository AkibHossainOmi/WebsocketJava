package com.tb.common.eventDriven;

public interface RequestStatusListener {
    // Called when a response is received for a tracked request
    void onResponseReceived(Expirable event);

    // Called when an event expires without receiving a response
    void onEventExpired(Expirable event);
}
