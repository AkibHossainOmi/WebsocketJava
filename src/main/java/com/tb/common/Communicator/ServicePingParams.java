package com.tb.common.Communicator;

import java.util.concurrent.TimeUnit;

public class ServicePingParams {
    public TimeUnit timeUnit;
    public int initialDelay;
    public int period;
    public int schedulerTerminationWaitPeriod=0;
    public int consecutiveExpireCountForServiceDown=3;
    public int consecutiveResponseCountForServiceUp=3;
    public int maxEventToStoreForHealthCount=10;
    public boolean throwOnDuplicateEvent=true;
}
