package com.tb.common.eventDriven.RequestAndResponse.Enums;

import com.tb.common.eventDriven.RequestAndResponse.PayloadType;

public enum ServicePacket implements PayloadType {
    ServicePing,
    ServicePingResp,
    ServiceUp,
    ServiceDown,
    ServiceError,
    ServiceStatus,
    Payload
}
