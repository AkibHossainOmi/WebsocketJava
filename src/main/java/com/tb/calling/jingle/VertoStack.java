package com.tb.calling.jingle;

import com.tb.calling.base.AbstractCallStack;
import com.tb.calling.base.statemachine.Transition;
import com.tb.calling.verto.VertoCallLeg;
import com.tb.calling.verto.VertoChannel;
import com.tb.calling.verto.VertoSettings;
import com.tb.common.SignalingEvent;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;
import com.tb.transport.rest.RestSettings;
import com.tb.transport.xmpp.XmppSettings;

public class VertoStack extends AbstractCallStack {
    public VertoStack(VertoSettings vertoSettings, VertoChannel channel) {
        super(channel);
    }
    @Override
    public void onSignalingMessage(SignalingEvent msg) {
        CallEventType eventType=msg.getCallEventType();
        switch (eventType) {
            case SESSION_START -> {
                if(super.getCalls().get(msg.getSessionId())==null){
                    if(this.getCalls().containsKey(msg.getSessionId())) return;//ignore session start if call exists
                    VertoCallLeg vertoCallLeg= new VertoCallLeg(this);
                    this.getCalls().put(vertoCallLeg.getSessionId(),vertoCallLeg);
                }
            }
            case TRYING -> {
            }
            case RINGING -> {
            }
            case ANSWER -> {
            }
            case HANGUP -> {
            }
            case SDP -> {
            }
            case ICE_CANDIDATE -> {
            }
            case ICE_CANDIDATE_ACK -> {
            }
            case UNKNOWN -> {
            }
        }
    }
    @Override
    protected void onCallStateChange(String sessionId, Transition transition, SignalingEvent msg){
        //per call state change
    }
    @Override
    protected void onCallInbandStateMessage(String sessionId, CallState state, SignalingEvent msg){

    }
}
