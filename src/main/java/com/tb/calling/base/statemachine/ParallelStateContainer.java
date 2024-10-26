package com.tb.calling.base.statemachine;

import com.tb.common.CallEvent;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;

import java.util.HashMap;
import java.util.Map;

public class ParallelStateContainer {
    private final Map<StateMachineType, CallStateMachine> stateMachines;
    public ParallelStateContainer() {
        this.stateMachines = new HashMap<>();
    }

    // Method to add a state machine to the container
    public void addStateMachine(StateMachineType stateMachineType,
                                CallStateMachine stateMachine) {
        stateMachines.put(stateMachineType, stateMachine);
    }

    // Method to send an event to all state machines
    public void sendEvent(CallEvent event) {
        for (Map.Entry<StateMachineType, CallStateMachine> entry : stateMachines.entrySet()) {
            CallStateMachine stateMachine = entry.getValue();
            stateMachine.send(event);
        }
    }

    // Optional: Method to retrieve the current state of a specific state machine
    public CallState getState(StateMachineType stateMachineType) {
        CallStateMachine stateMachine = stateMachines.get(stateMachineType);
        return stateMachine != null ? stateMachine.getCurrentState() : null;
    }
}
