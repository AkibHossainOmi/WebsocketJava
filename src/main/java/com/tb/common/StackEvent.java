package com.tb.common;

import com.tb.common.eventDriven.RequestAndResponse.Enums.SignalingProtocol;
import com.tb.common.eventDriven.RequestAndResponse.Enums.StackEventType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class StackEvent{
    final StackEventType eventType;
    final SignalingProtocol signalingProtocol;
    final List< SignalingEvent> relatedSignalingEvents= new ArrayList<>();

    public StackEvent(StackEventType eventType, SignalingProtocol signalingProtocol,
                      List<SignalingEvent> relatedSignalingEvents) {
        this.eventType = eventType;
        this.signalingProtocol = signalingProtocol;
        for (SignalingEvent signalingEvent : relatedSignalingEvents) {
            this.relatedSignalingEvents.add(signalingEvent);
        }
    }

    public StackEventType getEventType() {
        return eventType;
    }

    public SignalingProtocol getCallSignalingProtocol() {
        return signalingProtocol;
    }

    public List<SignalingEvent> getRelatedSignalingEvents() {
        return this.getRelatedSignalingEvents();
    }
    public String getSessionId(){
        return this.relatedSignalingEvents.get(0).getSessionId();
    }
}
