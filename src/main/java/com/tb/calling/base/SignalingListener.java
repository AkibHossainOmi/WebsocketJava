package com.tb.calling.base;

import com.tb.common.SignalingEvent;

public interface SignalingListener {
    void onSignalingMessage(SignalingEvent msg);
}
