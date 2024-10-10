package com.tb.common.eventDriven;

import com.tb.common.Communicator.Communicator;
import com.tb.common.Communicator.KeepAliveParams;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartbeatSender {
    private final ScheduledExecutorService scheduler;
    private Communicator communicator;
    private KeepAliveParams keepAliveParams;
    private String heartbeatId;
    private final ExpirableRequestFactory requestFactory; // Factory for creating new requests
    private boolean isRunning = false;
    public HeartbeatSender( KeepAliveParams keepAliveParams,
                            ExpirableRequestFactory requestFactory,
                            Communicator communicator) {
        this.keepAliveParams=keepAliveParams;
        this.requestFactory = requestFactory;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.communicator=communicator;
    }

    // Starts sending heartbeats periodically
    public void start() {
        if (isRunning) {
            throw new IllegalStateException("Heartbeat Sender is already running.");
        }
        isRunning = true;
        scheduler.scheduleAtFixedRate(() -> {
            ExpirableEvent request = requestFactory.createRequest();
            this.communicator.sendMessage(request);
        }, this.keepAliveParams.getInitialDelay(), keepAliveParams.getPeriod(), TimeUnit.SECONDS);
    }

    // Stops sending heartbeats
    public void stop() {
        isRunning = false;
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(keepAliveParams.getSchedulerTerminationWaitPeriod(),
                    keepAliveParams.getTimeUnit())) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
