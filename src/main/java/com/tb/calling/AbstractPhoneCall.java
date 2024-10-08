package com.tb.calling;


import java.io.IOException;
public abstract class AbstractPhoneCall implements PhoneCall {
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
    public AbstractPhoneCall(String uniqueId, String aparty, String bparty) {
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
}