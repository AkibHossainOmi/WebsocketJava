package com.tb.common.Communicator.InternalSocket;


import com.tb.common.Communicator.InternalSocket.TransportListener;
import com.tb.common.eventDriven.ExpirableEvent;

public interface Transport<T> {
    void addListener(TransportListener transportListener);

    void sendMessage(T message);
}
