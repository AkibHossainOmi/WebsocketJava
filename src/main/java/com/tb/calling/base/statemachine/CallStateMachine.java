package com.tb.calling.base.statemachine;

import com.tb.common.SignalingEvent;
import com.tb.common.Logger;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;
import com.tb.common.logging.LogSeverity;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CallStateMachine {
    private final Map<CallState, Map<CallEventType, Transition>> transitions = new HashMap<>();
    private CallState currentState;
    private final List<StateMachineListener> publicListeners;

    public CallStateMachine(CallState initialState, List<StateMachineListener> publicListeners) {
        this.currentState = initialState;
        this.publicListeners = publicListeners;
    }

    public CallStateMachine addTransition(CallState fromState, CallEventType eventType, CallState targetState) {
        Transition transition = new Transition(fromState, eventType, targetState);
        transitions.computeIfAbsent(fromState, k -> new HashMap<>()).put(eventType, transition);
        return this;
    }

    public void send(SignalingEvent event) {
        Map<CallEventType, Transition> stateTransitions = transitions.get(currentState);
        if (stateTransitions != null) {
            Transition transition = stateTransitions.get(event.getEventType());
            if (transition != null) {
                currentState = transition.getTargetState();
                notifyListeners(transition, event);  // Notify listeners about the state change
            } else {
                Logger.log(LogSeverity.WARNING, "No transition available for event: " + event.getEventType());
            }
        } else {
            Logger.log(LogSeverity.WARNING, "No transitions defined for current state: " + currentState);
        }
    }

    private void notifyListeners(Transition transition, SignalingEvent event) {
        for (StateMachineListener listener : publicListeners) {
            listener.onStateChange(transition, event);
        }
    }

    public CallState getCurrentState() {
        return currentState;
    }
}




