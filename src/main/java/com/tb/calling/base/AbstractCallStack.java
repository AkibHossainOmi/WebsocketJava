package com.tb.calling.base;

import com.tb.calling.base.statemachine.StateMachineListener;
import com.tb.calling.base.statemachine.Transition;
import com.tb.common.SignalingEvent;
import com.tb.common.StackEvent;
import com.tb.common.eventDriven.RequestAndResponse.Enums.SignalingProtocol;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;
import com.tb.common.eventDriven.RequestAndResponse.Enums.StackEventType;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractCallStack implements SignalingListener, StateMachineListener {
    private final SignalingProtocol signalingProtocol;
    private final Hashtable<String, AbstractCallLeg> calls;
    private final Channel channel;
    private final List<StackMsgListener> publicListeners = new CopyOnWriteArrayList<>();
    public AbstractCallStack(Channel channel) {
        this.calls = new Hashtable<>();
        this.channel= channel;
        this.channel.addPublicListener(this);
        this.signalingProtocol=channel.getSignalingProtocol();
    }
    public void addPublicListener(StackMsgListener listener){
        this.publicListeners.add(listener);
    }
    public void onStateChange(Transition transition, SignalingEvent msg){
        this.onCallStateChange(msg.getSessionId(), transition,msg);
    }//between this and a call
    public void onInbandStateMessage(CallState state, SignalingEvent msg){
        this.onCallInbandStateMessage(msg.getSessionId(), state,msg);
    }//between this and a call
    protected abstract void onCallStateChange(String sessionId, Transition transition, SignalingEvent msg);//between this and derived stack
    protected abstract void onCallInbandStateMessage(String sessionId, CallState state, SignalingEvent msg);//between this and derived stack
    protected void addCall(AbstractCallLeg call, List<SignalingEvent> relevantMsgs) {
        calls.put(call.getSessionId(), call);
        for (StackMsgListener publicListener : this.publicListeners) {
            StackEvent msg = new StackEvent(StackEventType.CALL_CREATED, this.signalingProtocol, relevantMsgs);
            publicListener.onStackMessage(msg);
        }
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

    public Channel getChannel() {
        return channel;
    }
}
