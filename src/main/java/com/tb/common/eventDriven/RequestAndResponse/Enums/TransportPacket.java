package com.tb.common.eventDriven.RequestAndResponse.Enums;

import com.tb.common.eventDriven.RequestAndResponse.PayloadType;

public enum TransportPacket implements PayloadType {
    TransportUp,
    TransportDown,
    TransportError,
    TransportStatus,
    Payload
}
