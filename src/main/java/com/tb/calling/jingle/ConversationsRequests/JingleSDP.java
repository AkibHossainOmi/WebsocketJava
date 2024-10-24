package com.tb.calling.jingle.ConversationsRequests;

import com.tb.calling.jingle.msgTemplates.SDPResponse;
import com.tb.common.StringUtil;
import com.tb.common.eventDriven.RequestAndResponse.Request;
import com.tb.common.eventDriven.RequestAndResponse.PayloadType;
import com.tb.common.eventDriven.RequestAndResponse.Payload;

public class JingleSDP extends Request {
    public JingleSDP(String data, PayloadType payloadType) {
        super(data, payloadType);
        String id= StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(data,"id='","'");
        this.setId(id);
    }
    @Override
    public Payload generateResponse() {
        String aParty = (String)this.getMetadata().get("aParty");
        String bParty = (String)this.getMetadata().get("bParty");
        assert(aParty!=null && bParty!=null);
        return new Payload(this.getId(),SDPResponse.createMessage(aParty,bParty,getId()),
                JingleMsgType.ICE_RESPONSE);
    }
}
