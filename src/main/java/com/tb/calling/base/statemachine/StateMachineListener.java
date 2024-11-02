package com.tb.calling.base.statemachine;

import com.tb.common.SignalingMessage;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;

public interface StateMachineListener {
    void onStateChange(Transition transition, SignalingMessage message);
    void onInbandStateMessage(CallState state,SignalingMessage message);
}