package com.tb.common;

import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;

public abstract class AbstractSignalingEvent<T> extends SignalingEvent {//T= dataType from transport e.g. String/protobuf
    final T rawData;
    public AbstractSignalingEvent(String sessionId, CallEventType eventType,
                                  boolean isFullyParsed, T rawData) {
        super(sessionId, eventType, isFullyParsed);
        if (sessionId.isEmpty())
            throw new RuntimeException("Found empty Session id in jingle "+eventType.toString());
        this.rawData = rawData;
    }
    public T getRawData(){return this.rawData;}
}
