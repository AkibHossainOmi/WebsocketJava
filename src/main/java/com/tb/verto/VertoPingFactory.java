package com.tb.verto;

import com.tb.common.eventDriven.ExpirableEvent;
import com.tb.common.eventDriven.ExpirableRequestFactory;
import com.tb.common.eventDriven.UniqueIntGenerator;
import com.tb.verto.msgTemplates.Ping;
import com.tb.webSocket.WsSentData;

public class VertoPingFactory implements ExpirableRequestFactory<WsSentData> {
    UniqueIntGenerator intGenerator
    @Override
    public WsSentData createRequest() {
        return new WsSentData(Ping.createMessage(pingSerialAsInput));
    }
}
