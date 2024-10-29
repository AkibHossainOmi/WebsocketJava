package com.tb.calling.verto;

import com.tb.common.StringUtil;


public class VertoSdpParamB {

    private String fingerprint;
    private String msid;
    private String ssrc;
    private String pwd;
    private String ufrag;
    private String ip;
    private int port;

    public VertoSdpParamB(String sdp) {
        this.pwd = StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(sdp, "=ice-pwd:", "a=candidate:").replace("\\r\\n", "").trim();


        this.ufrag = StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(sdp, "=ice-ufrag:", "a=ice-pwd:").replace("\\r\\n", "").trim();

        this.fingerprint = StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(sdp, "=fingerprint:sha-256", "a=setup").replace("\\r\\n", "").trim();


        this.msid = StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(sdp, "msid:", "a0").replace("\\r\\n", "").trim();


        this.ssrc = StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(sdp, "=ssrc:", " cname:").replace("\\r\\n", "").trim();

        this.port = Integer.parseInt(StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(sdp, "m=audio", "UDP/TLS").replace("\\r\\n", "").trim());

        this.ip = StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(sdp, "IN IP4", "s=FreeSWITCH").replace("\\r\\n", "").trim();

    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getMsid() {
        return msid;
    }

    public String getSsrc() {
        return ssrc;
    }

    public String getPwd() {
        return pwd;
    }

    public String getUfrag() {
        return ufrag;
    }
    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }



}
