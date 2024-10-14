package com.tb.common.eventDriven;

public interface Connector {
    void sendTransportMessage(Payload data);
    Payload createServicePingMsg();
    Payload createKeepAliveMsg();
    Transport getTransport();
    Expirable createRequestFromPayload(Payload payload);
    void connect();
    String getSessionId();

    void onServiceStatusChange(ServiceStatus status);
}

