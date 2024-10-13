package com.tb.common.eventDriven;

public interface Connector {
    void onMessage(Payload data);
    void sendMessage(Payload data);
    void onServiceDown(Payload data);
    void onServiceUp(Payload data);
    Payload createServicePingMsg();
    Payload createKeepAliveMsg();
    Transport getTransport();
    Expirable createRequestFromPayload(Payload payload);
    void onServiceStatusChange(ServiceStatus status); // "up" or "down"
    void connect();
    String getSessionId();
}

