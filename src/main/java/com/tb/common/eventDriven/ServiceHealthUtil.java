package com.tb.common.eventDriven;

public interface ServiceHealthUtil {
    ExpirableEvent createKeepAliveMsg();//both may have same implementation
    ExpirableEvent createServicePingMsg();
}

