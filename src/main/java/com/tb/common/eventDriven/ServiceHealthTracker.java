package com.tb.common.eventDriven;

import com.tb.common.Communicator.Connector;
import com.tb.common.Communicator.Payload;
import com.tb.common.Communicator.ServiceKeepAliveParams;
import com.tb.common.Communicator.ServicePingParams;
import com.tb.common.Communicator.InternalSocket.Transport;
import com.tb.verto.ServiceHealthCounter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ServiceHealthTracker {
    enum PingOrKeepAlive {PING, KEEP_ALIVE}
    private final ScheduledExecutorService keepAliveScheduler = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService servicePingScheduler = Executors.newSingleThreadScheduledExecutor();
    private ServicePingParams pingParams;
    private ServiceKeepAliveParams keepAliveParams;
    private boolean pingRunning = false;
    private boolean keepAliveRunning = false;
    private Connector connector;
    ServiceHealthCounter healthCounter;

    public ServiceHealthTracker(Transport transport, ServicePingParams pingParams,
                                ServiceKeepAliveParams keepAliveParams,
                                Connector connector) {
        this.pingParams = pingParams;
        this.keepAliveParams=keepAliveParams;
        this.connector=connector;
        this.healthCounter = new ServiceHealthCounter(pingParams);
    }
    public void startServicePingMonitor() {
        if (this.pingParams==null){
            throw new RuntimeException("Cannot start service ping monitor because params are empty.");
        }
        startScheduler(PingOrKeepAlive.PING);
    }

    public void startKeepAliveSender() {
        if (this.keepAliveParams==null){
            throw new RuntimeException("Cannot start keep alive sender because params are empty.");
        }
        startScheduler(PingOrKeepAlive.KEEP_ALIVE);
    }

    void startScheduler(PingOrKeepAlive pingOrKeepAlive) {
        if (pingOrKeepAlive == PingOrKeepAlive.PING) {
            if (pingRunning) return;
            servicePingScheduler.scheduleAtFixedRate(() -> {
                Payload payload= connector.createServicePingMsg();
                ExpirableEvent requestToTrack= connector.createRequestFromPayload(payload);
                this.eventStore.add(requestToTrack);
                this.connector.getTransport().sendMessage(request);
            }, this.pingParams.initialDelay, pingParams.period, pingParams.timeUnit);
        } else {
            if (keepAliveRunning) return;
            keepAliveScheduler.scheduleAtFixedRate(() -> {
                T request = connector.createKeepAliveMsg();
                this.connector.getTransport().sendMessage(request);
            }, keepAliveParams.initialDelay, keepAliveParams.period, keepAliveParams.timeUnit);
        }
    }
    public void stopServicePingMonitor() {
        try {
            if (!servicePingScheduler.awaitTermination(pingParams.schedulerTerminationWaitPeriod, pingParams.timeUnit)) {
                servicePingScheduler.shutdownNow();
                pingRunning = false;
            }
        } catch (InterruptedException e) {
            servicePingScheduler.shutdownNow();
            Thread.currentThread().interrupt();
            pingRunning = false;
        }
    }
    public void stopKeepAliveSender() {
        try {
            if (!keepAliveScheduler.awaitTermination(keepAliveParams.schedulerTerminationWaitPeriod, keepAliveParams.timeUnit)) {
                keepAliveScheduler.shutdownNow();
                keepAliveRunning = false;
            }
        } catch (InterruptedException e) {
            keepAliveScheduler.shutdownNow();
            Thread.currentThread().interrupt();
            keepAliveRunning = false;
        }
    }
    public void sendAdhocServicePing(ExpirableEvent request){
        this.connector.getTransport().sendMessage(request);
    }
    public void sendAdhocKeepAlive(ExpirableEvent request){
        this.connector.getTransport().sendMessage(request);
    }
}
