package com.tb.common.eventDriven;

public interface ServiceHealthUtil {
    ExpirableEvent createKeepAlive();//both may have same implementation
    ExpirableEvent createServicePing();
}

