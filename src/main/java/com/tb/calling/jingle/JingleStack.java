package com.tb.calling.jingle;

import com.tb.calling.base.AbstractCallStack;
import com.tb.calling.base.statemachine.Transition;
import com.tb.common.SignalingEvent;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;
import com.tb.transport.rest.RestSettings;
import com.tb.transport.xmpp.XmppSettings;

import java.util.Arrays;

public class JingleStack extends AbstractCallStack {
    private final RestSettings restSettings;
    private final XmppSettings xmppSettings;
    public JingleStack(RestSettings restSettings,
                       XmppSettings xmppSettings, JingleChannel channel) {
        super(channel);
        this.restSettings= restSettings;
        this.xmppSettings= xmppSettings;
    }
    @Override
    public void onSignalingMessage(SignalingEvent msg) {
        CallEventType eventType=msg.getCallEventType();
        switch (eventType) {
            case SESSION_START -> {
                if(super.getCalls().get(msg.getSessionId())==null){
                    if(this.getCalls().containsKey(msg.getSessionId())) return;//ignore session start if call exists
                    JingleCallLeg jingleCall= new JingleCallLeg(this);
                    this.addCall(jingleCall, Arrays.asList(msg));
                }
            }
            case SDP -> {
            }
            case ICE_CANDIDATE -> {
            }
            case ICE_CANDIDATE_ACK -> {
            }
            case UNKNOWN -> {
            }
            default -> {

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

    public RestSettings getRestSettings() {
        return restSettings;
    }

    public XmppSettings getXmppSettings() {
        return xmppSettings;
    }
}
