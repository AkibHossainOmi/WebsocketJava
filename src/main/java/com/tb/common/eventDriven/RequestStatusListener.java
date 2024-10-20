package com.tb.common.eventDriven;
import com.tb.common.eventDriven.RequestAndResponse.Payload;

public interface RequestStatusListener {
    // Called when a response is received for a tracked request
    void onResponseReceived(Payload payload);

    // Called when an event expires without receiving a response
    void onEventExpired(Payload payload);
}
