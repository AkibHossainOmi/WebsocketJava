package com.tb.common.Communicator;

import com.tb.webSocket.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Communicator implements
        Transport<WsOnOpenData, WsOnCloseData, WsOnMsgData, WsSentData, WsOnErrorData, WsOnStatusData>{
    ConnectionStatus connectionStatus=ConnectionStatus.closed;
    protected void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }
    boolean autoReconnect=true;
    public boolean isAutoReconnect() {
        return autoReconnect;
    }
    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }
    public KeepAliveParams getKeepAliveParam() {
        return keepAliveParams;
    }

    KeepAliveParams keepAliveParams;
    protected void setKeepAliveMethod(Runnable keepAliveMethod) {
        this.keepAliveMethod = keepAliveMethod;
    }
    protected void setConnectMethod(Runnable connectMethod) {
        this.connectMethod =connectMethod;
    }
    Runnable keepAliveMethod;
    Runnable connectMethod;
    protected ScheduledExecutorService scheduler;
    public Communicator(KeepAliveParams keepAliveParams) {//constructor
        this.keepAliveParams = keepAliveParams;
    }
    public void startKeepAlive() {
        if(this.keepAliveParams !=null) {
            this.scheduler = Executors.newScheduledThreadPool(1);
            this.scheduler.scheduleAtFixedRate(this.keepAliveMethod,
                    keepAliveParams.initialDelay, keepAliveParams.period,
                    TimeUnit.SECONDS); // Initial delay of 0 seconds, repeat every 1 second
        }
    }
    public abstract Transport connectOrReconnect();
    @Override
    public abstract void onOpen(WsOnOpenData data);
    public abstract void onClose(WsOnCloseData data);
    @Override
    public abstract void onMessage(WsOnMsgData wsOnMsgData);
    @Override
    public abstract void sendMessage(WsSentData wsSentData);
    @Override
    public abstract void onError(WsOnErrorData wsOnErrorData);
    @Override
    public abstract void onStatus(WsOnStatusData data);
    public void stopKeepAlive() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}
