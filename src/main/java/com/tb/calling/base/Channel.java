package com.tb.calling.base;

import com.tb.common.SignalingEvent;
import com.tb.common.eventDriven.RequestAndResponse.Enums.SignalingProtocol;
import com.tb.common.eventDriven.RequestAndResponse.MultiThreadedRequestHandler;

public interface Channel {
    void sendSignalingMessage(SignalingEvent msg);
    void addPublicListener(SignalingListener listener);
    SignalingProtocol getSignalingProtocol();
    MultiThreadedRequestHandler getMultiThreadedRequestHandler();
}
