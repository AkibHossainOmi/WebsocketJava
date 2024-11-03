package com.tb.calling.base;

import com.tb.calling.base.statemachine.StateMachineListener;
import com.tb.calling.base.statemachine.Transition;
import com.tb.common.SignalingEvent;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;

public class TwoWayCallBridge {
    AbstractCallLeg origLeg;
    AbstractCallLeg termLeg;
    AbstractCallStack origStack;
    AbstractCallStack termStack;
    StateMachineListener origListener;
    StateMachineListener termListener;
    public TwoWayCallBridge(AbstractCallLeg origLeg, AbstractCallLeg termLeg,
                            AbstractCallStack origStack,AbstractCallStack termStack) {
        this.origLeg = origLeg;
        this.termLeg = termLeg;
        this.origStack=origStack;
        this.termStack=termStack;
        this.origListener= createOrigStateListener(this);
        this.termListener= createOrigStateListener(this);
        origLeg.addStateListener(origListener);
        termLeg.addStateListener(termListener);
    }

    private static StateMachineListener createOrigStateListener(TwoWayCallBridge bridge) {
        return new StateMachineListener() {
            @Override
            public void onStateChange(Transition transition, SignalingEvent message) {

            }
            @Override
            public void onInbandStateMessage(CallState state, SignalingEvent message) {

            }
        };
    }


    public AbstractCallLeg getOrigLeg() {
        return origLeg;
    }

    public AbstractCallLeg getTermLeg() {
        return termLeg;
    }
}
