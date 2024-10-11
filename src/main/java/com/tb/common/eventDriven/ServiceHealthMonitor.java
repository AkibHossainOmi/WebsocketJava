package com.tb.common.eventDriven;

import com.tb.common.Communicator.Connector;
import com.tb.common.Communicator.ServiceKeepAliveParams;
import com.tb.common.Communicator.ServicePingParams;
import com.tb.common.Communicator.Transport;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ServiceHealthMonitor {
    enum PingOrKeepAlive {PING, KEEP_ALIVE}

    ;
    private final ScheduledExecutorService keepAliveScheduler = Executors.newSingleThreadScheduledExecutor();
    private final ScheduledExecutorService servicePingScheduler = Executors.newSingleThreadScheduledExecutor();
    //private final ScheduledExecutorService scheduler;
    private ServicePingParams pingParams;
    private ServiceKeepAliveParams keepAliveParams;
    private final ServiceHealthUtil serviceHealthUtil; // Factory for creating new requests
    private final Transport transport;
    private EventStore eventStore;
    private boolean pingRunning = false;
    private boolean keepAliveRunning = false;
    private final List<Connector> listeners = new CopyOnWriteArrayList<>();

    public ServiceHealthMonitor(Transport transport, ServicePingParams pingParams,
                                ServiceKeepAliveParams keepAliveParams,
                                ServiceHealthUtil serviceHealthUtil) {
        this.transport = transport;
        this.pingParams = pingParams;
        this.keepAliveParams=keepAliveParams;
        this.serviceHealthUtil = serviceHealthUtil;
    }

    public void addListener(Connector connector) {
        this.listeners.add(connector);
    }

    // Starts sending heartbeats periodically
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
                ExpirableEvent request = serviceHealthUtil.createServicePing();
                this.transport.sendMessage(request);
            }, this.pingParams.initialDelay, pingParams.period, pingParams.timeUnit);
        } else {
            if (keepAliveRunning) return;
            keepAliveScheduler.scheduleAtFixedRate(() -> {
                ExpirableEvent request = serviceHealthUtil.createKeepAlive();
                this.transport.sendMessage(request);
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
        this.transport.sendMessage(request);
    }
    public void sendAdhocKeepAlive(ExpirableEvent request){
        this.transport.sendMessage(request);
    }
}
