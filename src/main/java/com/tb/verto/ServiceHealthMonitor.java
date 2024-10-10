package com.tb.verto;

import com.tb.common.eventDriven.EventListener;
import com.tb.common.eventDriven.ExpirableEvent;
import com.tb.common.eventDriven.ServiceStatus;
import com.tb.common.eventDriven.ServiceStatusListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceHealthMonitor implements EventListener {
    private ServiceStatus serviceStatus;
    private LocalDateTime lastStatusChangedOn;
    private final int consecutiveExpireCountForServiceDown;
    private final int consecutiveResponseCountForServiceUp;
    private int consecutiveExpiryCount = 0;
    private int consecutiveResponseCount = 0;
    private final List<ServiceStatusListener> listeners = new ArrayList<>();
    public ServiceHealthMonitor(int consecutiveExpireCountForServiceDown, int consecutiveResponseCountForServiceUp) {
        this.consecutiveExpireCountForServiceDown = consecutiveExpireCountForServiceDown;
        this.consecutiveResponseCountForServiceUp = consecutiveResponseCountForServiceUp;
        this.serviceStatus = ServiceStatus.DOWN;  // Assume service starts in "up" state
        this.lastStatusChangedOn=LocalDateTime.now();
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
            markServiceUp();  // Mark the service as "up"
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
            markServiceDown();  // Mark the service as "down"
        }
    }

    private void markServiceUp() {
        if (serviceStatus != ServiceStatus.UP) {
            serviceStatus = ServiceStatus.UP;
            consecutiveResponseCount = 0;  // Reset consecutive response count
            notifyListeners(ServiceStatus.UP);  // Notify listeners about service status change to "up"
        }
    }
    private void markServiceDown() {
        if (serviceStatus != ServiceStatus.DOWN) {
            serviceStatus = ServiceStatus.DOWN;
            consecutiveExpiryCount = 0;  // Reset consecutive expiry count
            notifyListeners(ServiceStatus.DOWN);  // Notify listeners about service status change to "down"
        }
    }
    private void notifyListeners(ServiceStatus status) {
        for (ServiceStatusListener listener : listeners) {
            listener.onServiceStatusChange(status);
        }
    }
    public void addListener(ServiceStatusListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ServiceStatusListener listener) {
        listeners.remove(listener);
    }
}

