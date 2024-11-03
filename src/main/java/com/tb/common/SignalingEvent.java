package com.tb.common;

import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;

import java.time.LocalDateTime;

public abstract class SignalingEvent {
    final CallEventType eventType;
    protected String sessionId;
    final boolean isFullyParsed;
    final LocalDateTime eventTime;
    public SignalingEvent(String sessionId, CallEventType eventType, boolean isFullyParsed) {
        this.sessionId = sessionId;
        this.eventType = eventType;
        this.isFullyParsed = isFullyParsed;
        this.eventTime = LocalDateTime.now();
    }

    public CallEventType getCallEventType() {
        return this.eventType;
    }

    public Boolean isFullyParsed() {
        return this.isFullyParsed;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }
}
