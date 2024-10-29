package com.tb.calling.jingle;

import com.tb.calling.jingle.msgTemplates.ICE;
import com.tb.calling.jingle.msgTemplates.SDP;

public class SDPMessageFactory {

    private final String sid;
    private final String ufrag;
    private final String pwd;
    private final String fingerprint;
    private final String ssrc;
    private final String msid;
    private final String priority;
    private final String ip;
    private final int port;






    String aPartyWithDevice;
    String bPartyWithDevice;

    public SDPMessageFactory(String bPartyWithDevice, String aPartyWithDevice,
                             String sid, String ssrc,
                             String msid, String ufrag,
                             String pwd, String fingerprint, String priority, String ip, int port
    ) {
        this.bPartyWithDevice=bPartyWithDevice;
        this.aPartyWithDevice=aPartyWithDevice;
        this.sid = sid;
        this.ssrc = ssrc;
        this.msid = msid;
        this.ufrag = ufrag;
        this.pwd = pwd;
        this.fingerprint = fingerprint;
        this.priority = priority;
        this.ip = ip;
        this.port = port;
    }

    public String createSDPMessage() {
        return SDP.createMessage(bPartyWithDevice, aPartyWithDevice,sid,ssrc,msid,ufrag,pwd,fingerprint);
    }

    public String createSDPWithICEMessage() {
        return SDP.createMessage(bPartyWithDevice, aPartyWithDevice,sid,ssrc,msid,ufrag,pwd,fingerprint);
    }

    public String createICEMessage() {
        return ICE.createMessage(aPartyWithDevice, bPartyWithDevice, sid, pwd,priority,ip,port,ufrag);
    }

}
