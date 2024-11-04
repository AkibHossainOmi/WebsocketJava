package com.tb.calling.jingle.Conversations;

import com.tb.common.StringUtil;
import com.tb.common.eventDriven.RequestAndResponse.Payload;
import com.tb.common.eventDriven.RequestAndResponse.PayloadType;
import com.tb.common.eventDriven.RequestAndResponse.Request;

public class ProposeResponse extends Request {
    public ProposeResponse(String data, PayloadType payloadType) {
        super(data, payloadType);
        String id= StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(data,"id=",",");
        this.setId(id);
    }
    @Override
    public Payload generateResponse() {
        String aParty = (String)this.getMetadata().get("aParty");
        String bParty = (String)this.getMetadata().get("bParty");
        assert(aParty!=null && bParty!=null);
        return new Payload(this.getId(), com.tb.calling.jingle.message.templates.ProposeResponse.createMessage(bParty,aParty,getId()),
                JingleMsgType.ICE_RESPONSE);
    }
}
