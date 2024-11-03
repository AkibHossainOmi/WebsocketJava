package com.tb.calling.base;

import com.tb.common.SignalingEvent;

public interface Channel {
    void sendSignalingMessage(SignalingEvent msg);
    void addPublicListener(SignalingListener listener);
}
