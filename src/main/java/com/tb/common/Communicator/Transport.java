package com.tb.common.Communicator;

public interface Transport<TOpenData, TCloseData,TOnMsgData, TSentData, TOnErrorData, TOnStatusData,
        TNotifyData> {
    void onOpen(TOpenData data);
    void onClose(TCloseData data);
    void onMessage(TOnMsgData data);
    void sendMessage(TSentData data);
    void onError(TOnErrorData data);
    void onStatus(TOnStatusData data);
    void connect();
    void notifyListeners(TNotifyData data);
    void close();
    ConnectionStatus getConnectionStatus();
}
