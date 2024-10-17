package com.tb.calling.verto;

import com.tb.common.ServiceEnum.PayloadType;
import com.tb.transport.websocket.WebSocketProxy;
import com.tb.calling.verto.msgTemplates.Login;
import com.tb.calling.verto.msgTemplates.Ping;
import com.tb.common.ServiceEnum.TransportPacket;
import com.tb.common.ServiceEnum.VertoPacket;
import com.tb.common.eventDriven.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VertoConnector implements Connector{
    //Transport webSocket;
    VertoConnectParams params;

    public ServiceHealthTracker getServiceHealthTracker() {
        return serviceHealthTracker;
    }

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
            public void onTransportOpen(Payload payload) {

            }

            @Override
            public void onTransportClose(Payload payload) {
            }

            @Override
            public void onTransportMessage(Payload payload) {
                if (payload.getPayloadType()== TransportPacket.Payload){
                    if (payload.getData().contains("verto.ping")){
                        mySelf.getServiceHealthTracker().notify();
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
    public void sendTransportMessage(Payload payload) {
        this.transport.sendMessage(payload);
    }
}

