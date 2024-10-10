package com.tb.common.eventDriven;

public class EventDispatcher{
    EventStore store;
    Boolean trackResponse;
    EventListener serviceHealthMonitor;
    public EventDispatcher(Boolean trackResponse, int maxEventToTrackInStore,
                           EventListener serviceHealthMonitor) {
        this.trackResponse = trackResponse;
        this.store=trackResponse==true
                ?new EventStore(maxEventToTrackInStore,false)
                :null;
        this.serviceHealthMonitor = serviceHealthMonitor;
    }
    public void dispatch(ExpirableEvent event){
        dispatch(event,this);
    }
    void dispatch(ExpirableEvent event,EventDispatcher dispatcher){
        if(this.trackResponse){
            event.addListener(new EventListener() {
                @Override
                public void onResponseReceived(ExpirableEvent event) {
                    if (dispatcher.serviceHealthMonitor !=null){
                        serviceHealthMonitor.onResponseReceived(event);
                    }
                }
                @Override
                public void onEventExpired(ExpirableEvent event) {
                    serviceHealthMonitor.onEventExpired(event);
                }
            });
            this.store.add(event);
        }
        //dispatch here
    }
}
