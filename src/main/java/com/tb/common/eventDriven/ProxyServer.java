package com.tb.common.eventDriven;

public interface ProxyServer<TOpenData, TCloseData,TOnMsgData, TSentData, TOnErrorData, TOnStatusData>
{
    void onServerOpen(TOpenData data);
    void onServerClose(TCloseData data);
    void onServerMessage(TOnMsgData data);
    void sendMessageServer(TSentData data);
    void onServerError(TOnErrorData data);
    void onServerStatus(TOnStatusData data);
    ConnectionStatus getConnectionStatus();
}