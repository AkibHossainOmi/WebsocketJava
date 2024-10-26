package com.tb.calling.base.statemachine;

import com.tb.common.CallEvent;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;

import java.util.List;
import java.util.function.Supplier;

public class Transition {
    private final CallState fromState;
    CallEventType eventType;
    private final CallState targetState;
    public Transition(CallState fromState, CallEventType eventType, CallState targetState) {
        this.fromState = fromState;
        this.eventType = eventType;
        this.targetState = targetState;
    }
    public CallState getFromState() {
        return fromState;
    }
    public CallState getTargetState() {
        return targetState;
    }
    public CallEventType getEvent() {
        return this.eventType;
    }
}

