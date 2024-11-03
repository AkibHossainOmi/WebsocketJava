package com.tb.calling.base.statemachine;

import com.tb.common.SignalingEvent;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;

public interface StateMachineListener {
    void onStateChange(Transition transition, SignalingEvent message);
    void onInbandStateMessage(CallState state, SignalingEvent message);
}