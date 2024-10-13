package com.tb.common.eventDriven;

public interface PeerCommunicator<TRecvData, TSentData> {
    void sendData(TRecvData Data);
    void onMessage(TSentData data );
}
