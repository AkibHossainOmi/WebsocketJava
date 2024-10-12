package com.tb.verto;

import com.tb.common.Communicator.ServicePingParams;
import com.tb.common.eventDriven.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceHealthCounter implements EventListener {
    private EventStore eventStore;
    private ServiceStatus serviceStatus;
    private LocalDateTime lastStatusChangedOn;
    private final int consecutiveExpireCountForServiceDown;
    private final int consecutiveResponseCountForServiceUp;
    private int consecutiveExpiryCount = 0;
    private int consecutiveResponseCount = 0;
    private final List<ServiceStatusListener> listeners = new ArrayList<>();
    public ServiceHealthCounter(ServicePingParams pingParams, List<ServiceStatusListener> listeners){
        this.consecutiveExpireCountForServiceDown = pingParams.consecutiveExpireCountForServiceDown;
        this.consecutiveResponseCountForServiceUp = pingParams.consecutiveResponseCountForServiceUp;
        this.eventStore = new EventStore(pingParams.maxEventToStoreForHealthCount,
                pingParams.throwOnDuplicateEvent);
        for (ServiceStatusListener listener : listeners) {
            this.listeners.add(listener);
        }
        this.serviceStatus = ServiceStatus.DOWN;  // Assume service starts in "up" state
        this.lastStatusChangedOn=LocalDateTime.now();
    }
    public void addListener(ServiceStatusListener listener) {
        listeners.add(listener);
    }
    public void removeListener(ServiceStatusListener listener) {
        listeners.remove(listener);
    }
    @Override
    public void onResponseReceived(ExpirableEvent event) {
        if (serviceStatus == ServiceStatus.UP) {
            // If service is already up, ignore responses
            return;
        }
        consecutiveResponseCount++;
        consecutiveExpiryCount = 0;  // Reset expiry count when a response is received

        if (consecutiveResponseCount >= consecutiveResponseCountForServiceUp) {
            updateServiceStatus(ServiceStatus.UP);
            notifyListeners(ServiceStatus.UP);
        }
    }
    @Override
    public void onEventExpired(ExpirableEvent event) {
        if (serviceStatus == ServiceStatus.DOWN) {
            // If service is already down, ignore expirations
            return;
        }
        consecutiveExpiryCount++;
        consecutiveResponseCount = 0;  // Reset response count on expiration

        if (consecutiveExpiryCount >= consecutiveExpireCountForServiceDown) {
            updateServiceStatus(ServiceStatus.DOWN);
            notifyListeners(ServiceStatus.DOWN);
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

    private void notifyListeners(ServiceStatus status) {
        for (ServiceStatusListener listener : listeners) {
            listener.onServiceStatusChange(status);
        }
    }

}

