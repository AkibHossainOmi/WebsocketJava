package com.tb.calling.base.statemachine;

import com.tb.common.CallEvent;

public interface StateChangeListener {
    void onStateChange(Transition transition, CallEvent event);
}