package com.tb.calling.jingle.Conversations;

import com.tb.common.eventDriven.RequestAndResponse.PayloadType;

public enum JingleMsgType implements PayloadType {
    SDP,
    SDP_RESPONSE,
    ICE,
    ICE_RESPONSE,
    PROPOSE_RESPONSE
}
