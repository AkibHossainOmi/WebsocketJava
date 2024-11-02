package com.tb.calling.jingle.message.signaling;

import com.tb.common.AbstractSignalingMessage;
import com.tb.common.StringUtil;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;

public class JingleSDP extends AbstractSignalingMessage<String> {
    private String fingerprint;
    private String msid;
    private String ssrc;
    private String pwd;
    private String ufrag;
    public JingleSDP(String msg,boolean isFullyParsed) {
        super(StringUtil.Parser.getFirstOccuranceOfParamValueByIndexAndTerminatingStr
                        (msg,"jingle sid=","xmlns").trim()
                ,CallEventType.SDP,isFullyParsed,msg);
        if (isFullyParsed){
            this.pwd = StringUtil.Parser
                    .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "pwd=&apos;", "&apos;");
            this.ufrag = StringUtil.Parser
                    .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "ufrag=&apos;", "&apos;");
            this.fingerprint = StringUtil.Parser
                    .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "xmlns=&apos;urn:xmpp:jingle:apps:dtls:0&apos;&gt;", "&lt;/fingerprint&gt;");
            this.msid = StringUtil.Parser
                    .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "value=&apos;- audio-track-", "&apos;");
            this.ssrc = StringUtil.Parser
                    .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "ssrc=&apos;", "&apos;");
        }
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
