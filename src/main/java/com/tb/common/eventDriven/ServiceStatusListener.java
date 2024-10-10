package com.tb.common.eventDriven;

public interface ServiceStatusListener {
    void onServiceStatusChange(ServiceStatus status); // "up" or "down"
}