package com.tb.calling.base;


import com.tb.calling.base.statemachine.CallStateMachine;
import com.tb.calling.base.statemachine.StateChangeListener;
import com.tb.calling.base.statemachine.StateMachineType;
import com.tb.calling.base.statemachine.Transition;
import com.tb.common.CallEvent;
import com.tb.common.eventDriven.Connector;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;
import com.tb.common.eventDriven.TransportListener;

import java.util.Arrays;
import java.util.HashMap;

public abstract class AbstractCallLeg implements CallLeg, TransportListener, StateChangeListener {
    protected static final HashMap<StateMachineType, CallStateMachine> stateMachines = new HashMap<>();
    protected final HashMap<String, ICECandidate> remoteIceCandidates= new HashMap<>();
    public ICECandidate getRemoteIceCandidate(String id) {
        return this.remoteIceCandidates.get(id);
    }
    Connector connector;
    private String aParty;
    private String bParty;
    String sdp;
    private String uniqueId;
    private String aPartyDeviceId;
    private String bPartyDeviceId;
    public AbstractCallLeg(Connector connector) {
        this.connector=connector;
        // Initialize state machine
        CallStateMachine stateMachine =
                new CallStateMachine(CallState.IDLE, Arrays.asList(this));

        // Define transitions directly in the main
        stateMachine.addTransition(CallState.IDLE, CallEventType.SESSION_START, CallState.SESSION_START);
        stateMachine.addTransition(CallState.SESSION_START, CallEventType.HANGUP, CallState.HANGUP);
        stateMachine.addTransition(CallState.SESSION_START, CallEventType.TRYING, CallState.TRYING);
        stateMachine.addTransition(CallState.TRYING, CallEventType.HANGUP, CallState.HANGUP);
        stateMachine.addTransition(CallState.TRYING, CallEventType.RINGING, CallState.RINGING);
        stateMachine.addTransition(CallState.RINGING, CallEventType.HANGUP, CallState.HANGUP);
        stateMachine.addTransition(CallState.RINGING, CallEventType.ANSWER, CallState.ANSWERED);
    }
    public CallStateMachine getStateMachine(StateMachineType stateMachineType) {
        return stateMachines.get(stateMachineType);
    }
    @Override
    public abstract void onStateChange(Transition transition, CallEvent event);
    public void addRemoteIceCandidate(ICECandidate remoteIce) {
        this.remoteIceCandidates.put(remoteIce.getId(),remoteIce);
    }

    public Connector getConnector() {
        return connector;
    }


    public String getSdp() {
        return sdp;
    }

    public void setSdp(String sdp) {
        this.sdp = sdp;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getaParty() {
        return aParty;
    }

    public void setaParty(String aParty) {
        this.aParty = aParty;
    }

    public String getbParty() {
        return bParty;
    }

    public void setbParty(String bParty) {
        this.bParty = bParty;
    }
    public String getUniqueId() {
        return uniqueId;
    }
    public String getbPartyDeviceId() {
        return bPartyDeviceId;
    }
    public void setbPartyDeviceId(String bPartyDeviceId) {
        this.bPartyDeviceId = bPartyDeviceId;
    }
    public String getaPartyDeviceId() {
        return aPartyDeviceId;
    }

    public void setaPartyDeviceId(String aPartyDeviceId) {
        this.aPartyDeviceId = aPartyDeviceId;
    }

}