package com.tb.calling.base;

import com.tb.common.AbstractSignalingMessage;
import com.tb.common.SignalingMessage;

public interface Channel {
    void sendSignalingMessage(SignalingMessage msg);
    void addPublicListener(ChannelListener listener);
}
