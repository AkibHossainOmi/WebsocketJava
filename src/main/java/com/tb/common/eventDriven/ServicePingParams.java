package com.tb.common.eventDriven;

import java.util.concurrent.TimeUnit;

public class ServicePingParams {
    public TimeUnit timeUnit=TimeUnit.SECONDS;
    public int initialDelay=0;
    public int period=1;
    public int schedulerTerminationWaitPeriod=0;
    public int consecutiveExpireCountForServiceDown=3;
    public int consecutiveResponseCountForServiceUp=3;
    public int maxEventToStoreForHealthCount=50;
    public boolean throwOnDuplicateEvent=true;
}
