package com.tb.calling.jingle.message.signaling;

import com.tb.common.AbstractSignalingEvent;
import com.tb.common.StringUtil;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;

public class JingleICE extends AbstractSignalingEvent<String> {
    public JingleICE(String msg, boolean isFullyParsed) {
        super(StringUtil.Parser
                        .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "priority=&apos;", "&apos;"),
                CallEventType.ICE_CANDIDATE,isFullyParsed, msg);

    }

}
