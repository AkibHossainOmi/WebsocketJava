package com.tb.common.eventDriven;

public interface ProxyClient<TOpenData, TCloseData,TOnMsgData, TSentData, TOnErrorData, TOnStatusData>
{
    void onClientOpen(TOpenData data);
    void onClientClose(TCloseData data);
    void onClientMessage(TOnMsgData data);
    void SendMessageClient(TSentData data);
    void onClientError(TOnErrorData data);
    void onClientStatus(TOnStatusData data);
    ConnectionStatus getConnectionStatus();
}