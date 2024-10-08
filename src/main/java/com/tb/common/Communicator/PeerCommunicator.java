package com.tb.common.Communicator;

public interface PeerCommunicator<TRecvData, TSentData> {
    void sendData(TRecvData Data);
    void onMessage(TSentData data );
}
