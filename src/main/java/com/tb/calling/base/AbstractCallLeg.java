package com.tb.calling.base;


import com.tb.calling.base.statemachine.CallStateMachine;
import com.tb.calling.base.statemachine.StateMachineListener;
import com.tb.calling.base.statemachine.StateMachineType;
import com.tb.calling.base.statemachine.Transition;
import com.tb.common.SignalingMessage;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;
import com.tb.common.eventDriven.TransportListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractCallLeg implements CallLeg, TransportListener, StateMachineListener {
    final AbstractCallStack stack;
    protected static final HashMap<StateMachineType, CallStateMachine> stateMachines = new HashMap<>();
    private final List<StateMachineListener> publicStateListeners = new CopyOnWriteArrayList<>();
    protected final HashMap<String, ICECandidate> remoteIceCandidates = new HashMap<>();
    public ICECandidate getRemoteIceCandidate(String id) {
        return this.remoteIceCandidates.get(id);
    }
    private String aParty;
    private String bParty;
    String sdp;
    private String sessionId;
    private String aPartyDeviceId;
    private String bPartyDeviceId;
    private String sourceDomain;          // Domain of the source party
    private String destinationDomain;     // Domain of the destination party
    private final HashMap<String, Object> metaData= new HashMap<>();
    private String codec;                 // Codec used for the call (e.g., "opus", "G711")
    private int pTime;                    // Packet time for the media stream
    private String callId;                // Unique identifier for the call
    private String correlationId;         // ID for tracking related messages
    private String mediaType;              // Type of media (e.g., "audio", "video")
    private String status;                 // Status of the call (e.g., "ringing", "answered", "ended")

    public AbstractCallLeg(AbstractCallStack stack) {
        this.stack= stack;
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

    public void addRemoteIceCandidate(ICECandidate remoteIce) {
        this.remoteIceCandidates.put(remoteIce.getId(), remoteIce);
    }

    @Override
    public abstract void onStateChange(Transition transition, SignalingMessage msg);
    @Override
    public abstract void onInbandStateMessage(CallState state, SignalingMessage msg);

    public CallStateMachine getStateMachine(StateMachineType stateMachineType) {
        return stateMachines.get(stateMachineType);
    }
    public String getSdp() {
        return sdp;
    }

    public void setSdp(String sdp) {
        this.sdp = sdp;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public String getSessionId() {
        return sessionId;
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

    public void addStateListener(StateMachineListener publicListener) {
        this.publicStateListeners.add(publicListener);
    }
    public HashMap<String, Object> getMetaData() {
        return metaData;
    }

    public String getSourceDomain() {
        return sourceDomain;
    }
    public void setSourceDomain(String sourceDomain) {
        this.sourceDomain = sourceDomain;
    }
    public String getDestinationDomain() {
        return destinationDomain;
    }
    public void setDestinationDomain(String destinationDomain) {
        this.destinationDomain = destinationDomain;
    }
    public String getCodec() {
        return codec;
    }
    public void setCodec(String codec) {
        this.codec = codec;
    }
    public int getPTime() {
        return pTime;
    }
    public void setPTime(int pTime) {
        this.pTime = pTime;
    }
    public String getCorrelationId() {
        return correlationId;
    }
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
    public String getMediaType() {
        return mediaType;
    }
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

}