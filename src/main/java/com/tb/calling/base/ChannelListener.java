package com.tb.calling.base;

import com.tb.common.SignalingMessage;

public interface ChannelListener {
    void onChannelMessage(SignalingMessage msg);
}
