package com.tb.common.Communicator;

import java.util.concurrent.TimeUnit;

public class KeepAliveParams {
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
    TimeUnit timeUnit;
    int initialDelay;
    int period;
    int schedulerTerminationWaitPeriod=0;
    public int getSchedulerTerminationWaitPeriod() {
        return schedulerTerminationWaitPeriod;
    }
    public KeepAliveParams(TimeUnit timeUnit, int initialDelay,
                           int period,int schedulerTerminationWaitPeriod) {
        this.timeUnit=timeUnit;
        this.initialDelay = initialDelay;
        this.period = period;
        this.schedulerTerminationWaitPeriod=schedulerTerminationWaitPeriod;
    }
    public int getInitialDelay() {
        return initialDelay;
    }
    public int getPeriod() {
        return period;
    }
}
