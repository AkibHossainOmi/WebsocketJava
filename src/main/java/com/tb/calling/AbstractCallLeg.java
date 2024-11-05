package com.tb.calling;


import com.tb.common.eventDriven.Connector;
import com.tb.common.eventDriven.TransportListener;

import java.io.IOException;
import java.util.HashMap;

public abstract class AbstractCallLeg implements CallLeg, TransportListener {
    public CallState getCallState() {
        return callState;
    }

    public void setCallState(CallState callState) {
        this.callState = callState;
    }

    public CallState callState;
    protected final HashMap<String, ICECandidate> remoteIceCandidates= new HashMap<>();

    public ICECandidate getRemoteIceCandidate(String id) {
        return this.remoteIceCandidates.get(id);
    }

    public void addRemoteIceCandidate(ICECandidate remoteIce) {
        this.remoteIceCandidates.put(remoteIce.getId(),remoteIce);
    }

    public Connector getConnector() {
        return connector;
    }

    Connector connector;

    public String getSdp() {
        return sdp;
    }

    public void setSdp(String sdp) {
        this.sdp = sdp;
    }

    String sdp;

    public String getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(String priorityId) {
        this.priorityId = priorityId;
    }

    private String priorityId;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    private String pwd;

    public String getUfrag() {
        return ufrag;
    }

    public void setUfrag(String ufrag) {
        this.ufrag = ufrag;
    }

    private String ufrag;

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    private String fingerprint;

    public String getMsId() {
        return msId;
    }

    public void setMsId(String msId) {
        this.msId = msId;
    }

    private String msId;

    public String getSsrc() {
        return ssrc;
    }

    public void setSsrc(String ssrc) {
        this.ssrc = ssrc;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    String phoneNumber;

    public String ssrc;
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
    private String uniqueId;


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

    private String aParty;
    private String bParty;

    public String getaPartyDeviceId() {
        return aPartyDeviceId;
    }

    public void setaPartyDeviceId(String aPartyDeviceId) {
        this.aPartyDeviceId = aPartyDeviceId;
    }

    private String aPartyDeviceId;

    public String getbPartyDeviceId() {
        return bPartyDeviceId;
    }

    public void setbPartyDeviceId(String bPartyDeviceId) {
        this.bPartyDeviceId = bPartyDeviceId;
    }

    private String bPartyDeviceId;
    private CallBridge callBridge;
    public abstract void onStart(Object message);
    public abstract void onNewMessage(Object message) throws IOException;
    public abstract void startSession();
    public abstract void updateSession();
    public abstract void disconnect();
    public abstract void onRing();
    public abstract void onAnswer();
    public AbstractCallLeg(Connector connector) {
        this.connector=connector;
        this.callState=CallState.SESSION_START;
    }
    public String getUniqueId() {
        return uniqueId;
    }
    public CallBridge getCallBridge() {
        return callBridge;
    }
    public void setCallBridge(CallBridge callBridge) {
        this.callBridge = callBridge;
    }
}