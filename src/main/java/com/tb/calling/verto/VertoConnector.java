package com.tb.calling.verto;

import com.tb.common.uniqueIdGenerator.UniqueIntGenerator;
import com.tb.transport.Transport;
import com.tb.transport.websocket.WebSocketTransport;
import com.tb.calling.verto.msgTemplates.Login;
import com.tb.calling.verto.msgTemplates.Ping;
import com.tb.common.eventDriven.RequestAndResponse.Enums.TransportPacket;
import com.tb.common.eventDriven.RequestAndResponse.Enums.VertoPacket;
import com.tb.common.eventDriven.*;
import com.tb.common.eventDriven.RequestAndResponse.Payload;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class VertoConnector implements Connector{
    public VertoSettings getParams() {
        return params;
    }

    //Transport webSocket;
    VertoSettings params;

    public ServiceHealthTracker getServiceHealthTracker() {
        return serviceHealthTracker;
    }

    ServiceHealthTracker serviceHealthTracker;
    WebSocketTransport transport;
    TransportListener transportListener;
    public List<TransportListener> getPublicListeners() {
        return publicListeners;
    }

    List<TransportListener> publicListeners = new CopyOnWriteArrayList<>();
    UniqueIntGenerator intGenerator= new UniqueIntGenerator(0);
    String vertoWebSocketSessionId;

    public int getPingExpiresInSec() {
        return pingExpiresInSec;
    }

    public void setPingExpiresInSec(int pingExpiresInSec) {
        this.pingExpiresInSec = pingExpiresInSec;
    }
    int pingExpiresInSec=2;
    public VertoConnector(VertoSettings params) {
        this.params=params;
        this.transportListener = createTransportListener(this);
        this.serviceHealthTracker =
                new ServiceHealthTracker(params.servicePingParams,null, this);
        serviceHealthTracker.startServicePingMonitor();

        //sendCall();
        //this.serviceHealthMonitor = new ServiceHealthMonitor(web params.servicePingParams);
        //sendHangup();
        //this.pingFactory= new VertoPingUtil();
    }
    @Override
    public void addPublicListener(TransportListener publicListener){
            this.publicListeners.add(publicListener);
    }
    @Override
    public void connectOrInit() {
        List<TransportListener>tl = new ArrayList<>();
        tl.add(this.transportListener);
        this.transport = new WebSocketTransport(params.webSocketSettings, tl);
        this.vertoWebSocketSessionId = UUID.randomUUID().toString();
        this.transport.connectOrInit();
    }

    @Override
    public String getSessionId() {
        return this.vertoWebSocketSessionId;
    }

    @Override
    public void onServiceStatusChange(ServiceStatus status) {

    }

    private TransportListener createTransportListener(VertoConnector mySelf) {
        return new TransportListener() {
            @Override
            public void onTransportOpen(Payload payload) {

            }

            @Override
            public void onTransportClose(Payload payload) {
            }

            @Override
            public void onTransportMessage(Payload payload) {
                System.out.println("Verto:" + payload.getData());
                if (payload.getPayloadType()== TransportPacket.Payload){
                    for (TransportListener publicListener : mySelf.getPublicListeners()) {
                        publicListener.onTransportMessage(payload);
                    }
                }
                if (payload.getPayloadType()== TransportPacket.Payload){
                   /* if (payload.getData().contains("verto.ping")){
                        mySelf.getServiceHealthTracker().notify();
                    }*/
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

    public void login() {
        String data =
                Login.createMessage(params.login, params.password,
                        params.webSocketSettings.getUri(),
                        vertoWebSocketSessionId,this.intGenerator.getNext());
        System.out.println(data);
        transport.sendMessage(new Payload(UUID.randomUUID().toString(),
                data, VertoPacket.Login));
    }
    public void ping() {
        String data =
                Ping.createMessage(intGenerator.getNext());
        System.out.println(data);
        transport.sendMessage(new Payload(UUID.randomUUID().toString(), data, VertoPacket.Ping));
    }
   /* private void sendCall() {
        callId = UUID.randomUUID().toString();
        String data =OutboundCall.createMessage(params.login,callId,sessionId,1);
        transport.sendMessage(data);
    }

    private void sendHangup(){
        String data =Hangup2.createMessage(callId,564);
        System.out.println(data);
        transport.sendMessage(data);
    }*/
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
        return this.transport;
    }

    @Override
    public Payload createRequestFromPayload(Payload payload) {
        return new Payload(intGenerator.getNext().toString(),payload.getData(),
                VertoPacket.Ping);
    }
    @Override
    public void sendMsgToConnector(Payload payload) {
        this.transport.sendMessage(payload);
    }
}

