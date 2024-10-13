package com.tb.common.eventDriven;

public interface TransportListener {

void onTransportOpen(Payload data);

void onTransportClose(Payload data);

void onTransportMessage(Payload data);

void onTransportError(Payload data);

void onTransportStatus(Payload data);
}