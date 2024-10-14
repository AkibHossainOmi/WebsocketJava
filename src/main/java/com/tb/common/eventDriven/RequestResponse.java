package com.tb.common.eventDriven;

import java.util.ArrayList;
import java.util.List;

public class RequestResponse {
    Payload request;
    final List<Payload> responses= new ArrayList<>();
    public void addResponse(Payload response) {
        this.responses.add(response);
    }

    public RequestResponse(Payload request) {
        this.request = request;
    }
}
