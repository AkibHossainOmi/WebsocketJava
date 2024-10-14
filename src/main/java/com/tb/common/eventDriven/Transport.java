package com.tb.common.eventDriven;


public interface Transport {
    void addListener(TransportListener transportListener);
    void sendMessage(Payload payload);
}
