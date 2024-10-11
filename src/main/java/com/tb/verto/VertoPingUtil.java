package com.tb.verto;

import com.tb.common.eventDriven.ServiceHealthUtil;
import com.tb.common.eventDriven.UniqueIntGenerator;
import com.tb.verto.msgTemplates.Ping;
import com.tb.webSocket.WsSentData;

public class VertoPingUtil implements ServiceHealthUtil<WsSentData> {
    UniqueIntGenerator intGenerator
    @Override
    public WsSentData createServicePingMsg() {
        return new WsSentData(Ping.createMessage(pingSerialAsInput));
    }
}
