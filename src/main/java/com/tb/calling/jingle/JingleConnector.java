package com.tb.calling.jingle;

import com.tb.calling.verto.VertoConnector;
import com.tb.common.ServiceEnum.TransportPacket;
import com.tb.transport.rest.RestSettings;
import com.tb.transport.rest.RestTransport;
import com.tb.transport.websocket.WebSocketTransport;
import com.tb.calling.verto.msgTemplates.Login;
import com.tb.calling.verto.msgTemplates.Ping;
import com.tb.common.ServiceEnum.VertoPacket;
import com.tb.common.eventDriven.*;
import com.tb.xmpp.XmppSettings;
import com.tb.xmpp.JingleTransportType;
import com.tb.xmpp.XmppTransport;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class JingleConnector implements Connector{
    RestTransport restTransport;
    RestSettings restSettings;
    XmppTransport xmppTransport;
    XmppSettings xmppSettings;
    JingleTransportType jingleTransportType;
    public ServiceHealthTracker getServiceHealthTracker() {
        return serviceHealthTracker;
    }
    ServiceHealthTracker serviceHealthTracker;
    TransportListener privateListener;

    public List<TransportListener> getPublicListeners() {
        return publicListeners;
    }

    List<TransportListener> publicListeners= new CopyOnWriteArrayList<>();
    UniqueIntGenerator intGenerator= new UniqueIntGenerator(0);
    public int getPingExpiresInSec() {
        return pingExpiresInSec;
    }
    public void setPingExpiresInSec(int pingExpiresInSec) {
        this.pingExpiresInSec = pingExpiresInSec;
    }
    int pingExpiresInSec=2;
    public JingleConnector(XmppSettings xmppSettings, RestSettings restSettings) {
        List<TransportListener> xmppListeners= Arrays.asList(privateListener);
        this.privateListener = createTransportListener(this);
        this.xmppSettings=xmppSettings;
        this.xmppTransport= new XmppTransport(xmppSettings,
                Arrays.asList(this.privateListener));
        this.serviceHealthTracker =
                new ServiceHealthTracker(xmppSettings.servicePingParams,null, this);
        serviceHealthTracker.startServicePingMonitor();
    }
    public void addListeners(List<TransportListener> publicListeners){
        for (TransportListener publicListener : publicListeners) {
            this.publicListeners.add(publicListener);
        }
    }
    @Override
    public void connect() {
        List<TransportListener>tl = new ArrayList<>();
        tl.add(this.privateListener);
        this.xmppTransport= new XmppTransport(this.xmppSettings,
                Arrays.asList(this.privateListener));
        this.xmppTransport.connect();
    }

    @Override
    public String getSessionId() {
        return null;
    }

    @Override
    public void onServiceStatusChange(ServiceStatus status) {

    }
    @Override
    public Payload createServicePingMsg() {
        return new Payload(intGenerator.getNext().toString(),
                Ping.createMessage(intGenerator.getNext()), VertoPacket.Ping);
    }
    @Override
    public Payload createKeepAliveMsg() {
        throw new RuntimeException("Method createKeepAliveMsg is not implemented");
    }

    @Override
    public Transport getTransport() {
        return this.xmppTransport;
    }

    @Override
    public Payload createRequestFromPayload(Payload payload) {
        return new Payload(intGenerator.getNext().toString(),payload.getData(),
                VertoPacket.Ping);
    }
    @Override
    public void sendTransportMessage(Payload payload) {
        HashMap<String, Object> metadata=payload.getMetadata();
        Boolean useRest= (Boolean) metadata.get("useRest");
        if (useRest){
            this.xmppTransport.sendMessage(payload);
        }
        else{
            this.restTransport.sendMessage(payload);
        }
    }
    private TransportListener createTransportListener(JingleConnector mySelf) {
        return new TransportListener() {
            @Override
            public void onTransportOpen(Payload payload) {

            }

            @Override
            public void onTransportClose(Payload payload) {
            }

            @Override
            public void onTransportMessage(Payload payload) {
                if (payload.getPayloadType()== TransportPacket.Payload){
                    for (TransportListener publicListener : mySelf.getPublicListeners()) {
                        publicListener.onTransportMessage(payload);
                    }
                }
            }

            @Override
            public void onTransportError(Payload payload) {

            }

            @Override
            public void onTransportStatus(Payload payload) {

            }
        };
    }
}

