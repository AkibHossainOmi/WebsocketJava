package com.tb.calling.jingle;

import com.tb.common.StringUtil;

public class JingleSdpParamA {

    private String fingerprint;
    private String msid;
    private String ssrc;
    private String pwd;
    private String ufrag;
    public JingleSdpParamA(String sdp) {
        this.pwd = StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(sdp, "pwd=&apos;", "&apos;");


        this.ufrag = StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(sdp, "ufrag=&apos;", "&apos;");

        this.fingerprint = StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(sdp, "xmlns=&apos;urn:xmpp:jingle:apps:dtls:0&apos;&gt;", "&lt;/fingerprint&gt;");


        this.msid = StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(sdp, "value=&apos;- audio-track-", "&apos;");


        this.ssrc = StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(sdp, "ssrc=&apos;", "&apos;");
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




}
