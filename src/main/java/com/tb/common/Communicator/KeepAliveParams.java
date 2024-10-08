package com.tb.common.Communicator;

public class KeepAliveParams {
    int initialDelaySec;
    int periodSec;
    public KeepAliveParams(int initialDelaySec, int periodSec) {
        this.initialDelaySec = initialDelaySec;
        this.periodSec = periodSec;
    }
    public int getInitialDelaySec() {
        return initialDelaySec;
    }
    public int getPeriodSec() {
        return periodSec;
    }
}
