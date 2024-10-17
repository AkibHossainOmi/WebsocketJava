package com.tb.calling;


import com.tb.common.eventDriven.Connector;
import com.tb.common.eventDriven.Payload;

import java.io.IOException;
public abstract class AbstractCallLeg implements CallLeg {
    Connector connector;

    public String getSdp() {
        return sdp;
    }

    public void setSdp(String sdp) {
        this.sdp = sdp;
    }

    String sdp;
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
    private String uniqueId;
    private String aParty;
    private String bParty;
    private CallBridge callBridge;
    public abstract void startCall() ;
    public abstract void onStart(Object message);
    public abstract void onNewMessage(Object message) throws IOException;
    public abstract void startSession(Object message);
    public abstract void updateSession();
    public abstract void disconnect();
    public abstract void onRing();
    public abstract void onAnswer();
    public AbstractCallLeg(Connector connector,
                           String uniqueId, String aParty, String bParty) {
        this.connector=connector;
        this.uniqueId = uniqueId;
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

    public abstract void onTransportMessage(Payload data);
}