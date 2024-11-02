package com.tb.calling.jingle;

import com.tb.calling.base.AbstractCallStack;
import com.tb.calling.base.statemachine.Transition;
import com.tb.calling.jingle.message.signaling.Propose;
import com.tb.common.AbstractSignalingMessage;
import com.tb.common.SignalingMessage;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;
import com.tb.transport.rest.RestSettings;
import com.tb.transport.xmpp.XmppSettings;

public class JingleStack extends AbstractCallStack {
    public JingleStack(RestSettings restSettings,
                       XmppSettings xmppSettings, JingleChannel channel) {
        super(restSettings,xmppSettings,channel);
    }
    @Override
    public void onChannelMessage(SignalingMessage msg) {
        CallEventType eventType=msg.getCallEventType();
        switch (eventType) {
            case SESSION_START -> {
                if(super.getCalls().get(msg.getSessionId())==null){
                    if(this.getCalls().containsKey(msg.getSessionId())) return;//ignore session start if call exists
                    JingleCallLeg jingleCall= new JingleCallLeg(this);
                    this.getCalls().put(jingleCall.getSessionId(),jingleCall);
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
    protected void onCallStateChange(String sessionId, Transition transition, SignalingMessage msg){
        //per call state change
    }
    @Override
    protected void onCallInbandStateMessage(String sessionId, CallState state, SignalingMessage msg){

    }
}
