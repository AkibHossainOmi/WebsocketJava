package com.tb.calling.jingle;

import com.tb.calling.jingle.msgTemplates.ICE1;
import com.tb.calling.jingle.msgTemplates.ICE2;
import com.tb.calling.jingle.msgTemplates.SDP;

public class SDPMessageFactory {

    private final String sid;
    private final String ufrag;
    private final String pwd;
    String aPartyWithDevice;
    String bPartyWithDevice;

    public SDPMessageFactory(String sid, String ufrag, String pwd,
                             String aPartyWithDevice, String bPartyWithDevice) {
        this.sid = sid;
        this.ufrag = ufrag;
        this.pwd = pwd;
        this.aPartyWithDevice=aPartyWithDevice;
        this.bPartyWithDevice=bPartyWithDevice;
    }

    public String createSDPMessage() {
        return SDP.createMessage(aPartyWithDevice, bPartyWithDevice,sid,ufrag,pwd);
    }

    public String createICE1Message() {
        return ICE1.createMessage(aPartyWithDevice, bPartyWithDevice, sid, pwd, ufrag);
    }

    public String createICE2Message() {
        return ICE2.createMessage(aPartyWithDevice, bPartyWithDevice, sid, pwd, ufrag);
    }
}
