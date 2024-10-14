package com.tb.common.ServiceEnum;

public enum ServicePacket implements PayloadType {
    ServicePing,
    ServicePingResp,
    ServiceUp,
    ServiceDown,
    ServiceError,
    ServiceStatus,
    Payload
}
