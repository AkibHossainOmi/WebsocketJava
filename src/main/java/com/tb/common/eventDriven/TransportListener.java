package com.tb.common.eventDriven;
import com.tb.common.eventDriven.RequestAndResponse.Payload;
public interface TransportListener {

void onTransportOpen(Payload payload);

void onTransportClose(Payload payload);

void onTransportMessage(Payload payload);

void onTransportError(Payload payload);

void onTransportStatus(Payload payload);
}