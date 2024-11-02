package com.tb.calling.base;

import com.tb.calling.base.statemachine.StateMachineListener;
import com.tb.calling.base.statemachine.Transition;
import com.tb.common.SignalingMessage;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;
import com.tb.transport.rest.RestSettings;
import com.tb.transport.xmpp.XmppSettings;

import java.util.Hashtable;

public abstract class AbstractCallStack implements ChannelListener, StateMachineListener {
    private final Hashtable<String, AbstractCallLeg> calls;
    private final Channel channel;
    private final RestSettings restSettings;
    private final XmppSettings xmppSettings;
    public AbstractCallStack(RestSettings restSettings,
                             XmppSettings xmppSettings, Channel channel) {
        this.calls = new Hashtable<>();
        this.restSettings= restSettings;
        this.xmppSettings= xmppSettings;
        this.channel= channel;
        this.channel.addPublicListener(this);
    }
    public void onStateChange(Transition transition, SignalingMessage msg){
        this.onCallStateChange(msg.getSessionId(), transition,msg);
    }//between this and a call
    public void onInbandStateMessage(CallState state, SignalingMessage msg){
        this.onCallInbandStateMessage(msg.getSessionId(), state,msg);
    }//between this and a call
    protected abstract void onCallStateChange(String sessionId, Transition transition, SignalingMessage msg);//between this and derived stack
    protected abstract void onCallInbandStateMessage(String sessionId, CallState state, SignalingMessage msg);//between this and derived stack
    public void addCall(AbstractCallLeg call) {
        calls.put(call.getSessionId(), call);
    }
    public void removeCall(String callId) {
        calls.remove(callId);
    }
    public AbstractCallLeg getCall(String callId) {
        return calls.get(callId);
    }
    public Hashtable<String, AbstractCallLeg> getCalls() {
        return calls;
    }
}
