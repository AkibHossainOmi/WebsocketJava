package com.tb.calling.base;

import com.tb.common.SignalingEvent;
import com.tb.common.StackEvent;

public interface StackMsgListener {
    void onStackMessage(StackEvent msg);
}
