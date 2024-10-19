package com.tb.calling.jingle;

import com.tb.common.ServiceEnum.TransportPacket;
import com.tb.transport.Transport;
import com.tb.transport.rest.RestSettings;
import com.tb.transport.rest.RestTransport;
import com.tb.calling.verto.msgTemplates.Ping;
import com.tb.common.ServiceEnum.VertoPacket;
import com.tb.common.eventDriven.*;
import com.tb.transport.xmpp.XmppSettings;
import com.tb.transport.xmpp.JingleTransportType;
import com.tb.transport.xmpp.XmppTransport;

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
        this.privateListener = createPrivateXmppListener(this);
        this.xmppSettings=xmppSettings;
        this.restSettings=restSettings;
        this.xmppTransport= new XmppTransport(xmppSettings,
                Arrays.asList(this.privateListener));
        this.serviceHealthTracker =
                new ServiceHealthTracker(xmppSettings.servicePingParams,null, this);
        if (xmppSettings.servicePingParams!=null)
        {
            serviceHealthTracker.startServicePingMonitor();
        }
    }
    @Override
    public void addListener(TransportListener publicListener){
            this.publicListeners.add(publicListener);
    }
    @Override
    public void connectOrInit() {
        this.xmppTransport= new XmppTransport(this.xmppSettings,
                Arrays.asList(this.createPrivateXmppListener(this)));
        this.xmppTransport.connectOrInit();
        this.restTransport= new RestTransport(this.restSettings,
                Arrays.asList(this.createPrivateRestListener(this)),
                this.restSettings.baseUrl);
        this.restTransport.connectOrInit();
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
    public void sendMsgToConnector(Payload payload) {
        HashMap<String, Object> metadata=payload.getMetadata();
        Boolean useRest= (Boolean) metadata.get("useRest");
        if (useRest){
            this.restTransport.sendMessage(payload);
        }
        else{
            this.restTransport.sendMessage(payload);
        }
    }
    private TransportListener createPrivateXmppListener(JingleConnector mySelf) {
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
    private TransportListener createPrivateRestListener(JingleConnector mySelf) {
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

