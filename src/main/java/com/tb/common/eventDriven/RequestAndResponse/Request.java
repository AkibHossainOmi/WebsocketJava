package com.tb.common.eventDriven.RequestAndResponse;

public abstract class Request extends Payload {

    public Request(String id, String data, PayloadType payloadType) {
        super(id, data, payloadType);

    }
    public Request(String data, PayloadType payloadType) {
        super(data, payloadType);
        String extractedId="";//extract id here
    }
    public abstract Payload generateResponse();
}
