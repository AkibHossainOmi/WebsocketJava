package com.tb.common.Communicator.InternalSocket;

public interface TransportListener<T> {

void onTransportOpen(T data);

void onTransportClose(T data);

void onTransportMessage(T data);

void onTransportError(T data);

void onTransportStatus(T data);
}