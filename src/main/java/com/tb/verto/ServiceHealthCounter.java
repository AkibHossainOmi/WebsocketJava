package com.tb.verto;

import com.tb.common.eventDriven.Connector;
import com.tb.common.eventDriven.ServicePingParams;
import com.tb.common.eventDriven.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceHealthCounter implements RequestStatusListener {
    private RequestStore requestStore;
    private ServiceStatus serviceStatus;
    private LocalDateTime lastStatusChangedOn;
    private final int consecutiveExpireCountForServiceDown;
    private final int consecutiveResponseCountForServiceUp;
    private int consecutiveExpiryCount = 0;
    private int consecutiveResponseCount = 0;
    private final List<Connector> publicListeners = new ArrayList<>();
    private RequestStatusListener requestStatusListener;
    public ServiceHealthCounter(ServicePingParams pingParams, List<Connector> publicListeners){
        this.consecutiveExpireCountForServiceDown = pingParams.consecutiveExpireCountForServiceDown;
        this.consecutiveResponseCountForServiceUp = pingParams.consecutiveResponseCountForServiceUp;

        for (Connector publicListener : publicListeners) {
            this.publicListeners.add(publicListener);
        }
        this.serviceStatus = ServiceStatus.DOWN;  // Assume service starts in "up" state
        this.lastStatusChangedOn=LocalDateTime.now();
        this.requestStatusListener= new RequestStatusListener() {
            @Override
            public void onResponseReceived(Payload payload) {

            }

            @Override
            public void onEventExpired(Payload payload) {

            }
        };
        this.requestStore = new RequestStore(pingParams.maxEventToStoreForHealthCount,
                pingParams.throwOnDuplicateEvent,
                new ArrayList<>(Arrays.asList(requestStatusListener)));
    }
    public void addListener(Connector publicListener) {
        publicListeners.add(publicListener);
    }
    public void removeListener(Connector publicListener) {
        publicListeners.remove(publicListener);
    }
    @Override
    public void onResponseReceived(Payload event) {
        if (serviceStatus == ServiceStatus.UP) {
            // If service is already up, ignore responses
            return;
        }
        consecutiveResponseCount++;
        consecutiveExpiryCount = 0;  // Reset expiry count when a response is received

        if (consecutiveResponseCount >= consecutiveResponseCountForServiceUp) {
            updateServiceStatus(ServiceStatus.UP);
            notifyPublicListeners(ServiceStatus.UP);
        }
    }
    @Override
    public void onEventExpired(Payload event) {
        if (serviceStatus == ServiceStatus.DOWN) {
            // If service is already down, ignore expirations
            return;
        }
        consecutiveExpiryCount++;
        consecutiveResponseCount = 0;  // Reset response count on expiration
        if (consecutiveExpiryCount >= consecutiveExpireCountForServiceDown) {
            updateServiceStatus(ServiceStatus.DOWN);
            notifyPublicListeners(ServiceStatus.DOWN);
        }
    }

    private void updateServiceStatus(ServiceStatus newStatus) {
        if (serviceStatus != newStatus) {
            serviceStatus = newStatus;
            if (newStatus == ServiceStatus.UP) {
                consecutiveResponseCount = 0;  // Reset consecutive response count
            } else if (newStatus == ServiceStatus.DOWN) {
                consecutiveExpiryCount = 0;  // Reset consecutive expiry count
            }
        }
    }
    private void notifyPublicListeners(ServiceStatus status) {
        for (Connector publicListener : publicListeners) {
            publicListener.onServiceStatusChange(status);
        }
    }
    public void addRequest(Payload request){
        this.requestStore.add(request);
    }
    public void addResponse(Payload response){
        this.requestStore.add(response);
    }
}

