package com.tb.verto;

import com.tb.WebSocketProxy;
import com.tb.common.ServiceEnum.VertoPacket;
import com.tb.common.eventDriven.*;
import com.tb.verto.msgTemplates.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VertoConnector implements Connector{
    //Transport webSocket;
    VertoConnectParams params;
    ServiceHealthTracker serviceHealthTracker;
    WebSocketProxy transport;
    TransportListener transportListener;
    UniqueIntGenerator intGenerator= new UniqueIntGenerator(0);
    String vertoWebSocketSessionId;

    public int getPingExpiresInSec() {
        return pingExpiresInSec;
    }

    public void setPingExpiresInSec(int pingExpiresInSec) {
        this.pingExpiresInSec = pingExpiresInSec;
    }
    int pingExpiresInSec=2;
    public VertoConnector(VertoConnectParams params) {
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
    public void connect() {
        List<TransportListener>tl = new ArrayList<>();
        tl.add(this.transportListener);
        this.transport = new WebSocketProxy(params.webSocketSettings, tl);
        this.vertoWebSocketSessionId = UUID.randomUUID().toString();
        this.transport.connect(this.transport);
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
            public void onTransportOpen(Payload data) {

            }

            @Override
            public void onTransportClose(Payload data) {

            }

            @Override
            public void onTransportMessage(Payload data) {

            }

            @Override
            public void onTransportError(Payload data) {

            }

            @Override
            public void onTransportStatus(Payload data) {

            }
        };
    }

    public void login() {
        String data =
                Login.createMessage(params.login, params.password,
                        params.webSocketSettings.getUri(),
                        vertoWebSocketSessionId,this.intGenerator.getNext());
        System.out.println(data);
        transport.sendMessage(new Payload(data, VertoPacket.Login));
    }
    public void ping() {
        String data =
                Ping.createMessage(intGenerator.getNext());
        System.out.println(data);
        transport.sendMessage(new Payload(data, VertoPacket.Ping));
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
        return new Payload(Ping.createMessage(intGenerator.getNext()), VertoPacket.Ping);
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
    public Expirable createRequestFromPayload(Payload payload) {
        return new Expirable(intGenerator.getNext().toString(),
                this.pingExpiresInSec,payload);
    }
    @Override
    public void sendTransportMessage(Payload data) {
        this.transport.sendMessage(data);
    }
}

