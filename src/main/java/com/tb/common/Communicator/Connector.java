package com.tb.common.Communicator;

import com.tb.WebSocketWrapper;
import com.tb.common.Communicator.InternalSocket.Transport;
import com.tb.common.eventDriven.ExpirableEvent;

public interface Connector {
    void onMessage(Payload data);
    void sendMessage(Payload data);
    void onServiceDown(Payload data);
    void onServiceUp(Payload data);
    Payload createServicePingMsg();
    Payload createKeepAliveMsg();
    Transport getTransport();
    ExpirableEvent createRequestFromPayload(Payload payload);
}

