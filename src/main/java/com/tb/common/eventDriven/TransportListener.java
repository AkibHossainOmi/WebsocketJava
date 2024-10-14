package com.tb.common.eventDriven;

public interface TransportListener {

void onTransportOpen(Payload payload);

void onTransportClose(Payload payload);

void onTransportMessage(Payload payload);

void onTransportError(Payload payload);

void onTransportStatus(Payload payload);
}