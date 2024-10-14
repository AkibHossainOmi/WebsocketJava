package com.tb.common.eventDriven;

public class EventDispatcher{
    RequestStore store;
    Boolean trackResponse;
    RequestStatusListener serviceHealthMonitor;
    public EventDispatcher(Boolean trackResponse, int maxEventToTrackInStore,
                           RequestStatusListener serviceHealthMonitor) {
        this.trackResponse = trackResponse;
        this.store=trackResponse==true
                ?new RequestStore(maxEventToTrackInStore,false)
                :null;
        this.serviceHealthMonitor = serviceHealthMonitor;
    }
    public void dispatch(ExpirableRequest event){
        dispatch(event,this);
    }
    void dispatch(ExpirableRequest event, EventDispatcher dispatcher){
        if(this.trackResponse){
            event.addListener(new RequestStatusListener() {
                @Override
                public void onResponseReceived(ExpirableRequest event) {
                    if (dispatcher.serviceHealthMonitor !=null){
                        serviceHealthMonitor.onResponseReceived(event);
                    }
                }
                @Override
                public void onEventExpired(ExpirableRequest event) {
                    serviceHealthMonitor.onEventExpired(event);
                }
            });
            this.store.add(event);
        }
        //dispatch here
    }
}
