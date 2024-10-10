package com.tb.verto;

import com.tb.common.eventDriven.EventListener;
import com.tb.common.eventDriven.ExpirableEvent;

import java.util.List;

public class VertoPing extends ExpirableEvent {

    public VertoPing(String id, int expirationInSeconds) {
        super(id, expirationInSeconds);
    }
    @Override
    public String getId() {
        return null;
    }
}
