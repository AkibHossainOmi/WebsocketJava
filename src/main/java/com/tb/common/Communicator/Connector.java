package com.tb.common.Communicator;

public interface Connector<TRecvData, TSentData> {
    void onMessage(TRecvData data);
    void sendMessage(TSentData data);
    void onServicedown(String data);
    void onServiceUp(String data);
}
