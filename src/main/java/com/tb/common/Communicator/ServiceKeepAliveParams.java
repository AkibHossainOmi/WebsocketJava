package com.tb.common.Communicator;

import java.util.concurrent.TimeUnit;

public class ServiceKeepAliveParams {
    public TimeUnit timeUnit;
    public int initialDelay;
    public int period;
    public int schedulerTerminationWaitPeriod=0;
}
