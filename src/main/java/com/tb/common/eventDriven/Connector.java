package com.tb.common.eventDriven;

public interface Connector {
    void sendTransportMessage(Payload payload);
    Payload createServicePingMsg();
    Payload createKeepAliveMsg();
    Transport getTransport();
    Payload createRequestFromPayload(Payload payload);
    void connect();
    String getSessionId();
    void onServiceStatusChange(ServiceStatus status);
}

