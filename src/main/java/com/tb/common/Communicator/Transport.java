package com.tb.common.Communicator;

public interface Transport<TOpenData, TCloseData,TOnMsgData, TSentData, TOnErrorData, TOnStatusData> {
    void onOpen(TOpenData data);
    void onClose(TCloseData data);
    void onMessage(TOnMsgData data);
    void sendMessage(TSentData data);
    void onError(TOnErrorData data);
    void onStatus(TOnStatusData data);
    ConnectionStatus getConnectionStatus();
}
