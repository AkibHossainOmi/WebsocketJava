package com.tb.verto;

import com.tb.WebSocketWrapper;
import com.tb.common.Communicator.Connector;
import com.tb.common.Communicator.InternalSocket.Transport;
import com.tb.common.Communicator.InternalSocket.TransportListener;
import com.tb.common.Communicator.Payload;
import com.tb.common.eventDriven.ExpirableEvent;
import com.tb.common.eventDriven.ServiceHealthTracker;
import com.tb.common.eventDriven.UniqueIntGenerator;
import com.tb.verto.msgTemplates.*;
import jdk.jshell.spi.ExecutionControl;

import java.util.Random;
import java.util.UUID;

class VertoConnector implements Connector{
    //Transport webSocket;
    VertoConnectParams params;
    ServiceHealthTracker serviceHealthTracker;
    WebSocketWrapper transport;
    TransportListener<String> transportListener;
    UniqueIntGenerator intGenerator= new UniqueIntGenerator(0);

    public int getPingExpiresInSec() {
        return pingExpiresInSec;
    }

    public void setPingExpiresInSec(int pingExpiresInSec) {
        this.pingExpiresInSec = pingExpiresInSec;
    }

    int pingExpiresInSec=2;
    public VertoConnector(VertoConnectParams params) {
        this.params=params;
        Random random = new Random();
        this.transport = new WebSocketWrapper(params.getWebSocketSettings());
        this.transportListener = createTransportListener(this);
        this.transport.addListener(this.transportListener);
        this.serviceHealthTracker =
                new ServiceHealthTracker<String>(this.transport,params.servicePingParams,
                null,this);
        serviceHealthTracker.startServicePingMonitor();
        login();
        //ping();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendCall();
        //this.serviceHealthMonitor = new ServiceHealthMonitor(web params.servicePingParams);
        sendHangup();
        //this.pingFactory= new VertoPingUtil();

    }

    private TransportListener createTransportListener(VertoConnector self) {
        return new TransportListener<String>() {
            @Override
            public void onTransportOpen(String data) {

            }

            @Override
            public void onTransportClose(String data) {

            }

            @Override
            public void onTransportMessage(String data) {
                self.onMessage(new Payload(data));
            }

            @Override
            public void onTransportError(String data) {

            }

            @Override
            public void onTransportStatus(String data) {

            }
        };
    }

    private void login() {
        sessionId = UUID.randomUUID().toString();
        String data =
                Login.createMessage(params.login, params.password,
                        params.webSocketSettings.getUri(),
                        sessionId,new Random().nextInt());
        transport.sendMessage(data);
    }
    private void sendCall() {
        callId = UUID.randomUUID().toString();
        String data =OutboundCall.createMessage(params.login,callId,sessionId,1);
        transport.sendMessage(data);
    }

    private void sendHangup(){
        String data =Hangup2.createMessage(callId,564);
        System.out.println(data);
        transport.sendMessage(data);
    }
    @Override
    public Payload createServicePingMsg() {
        return new Payload(Ping.createMessage(intGenerator.getNext()));
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
    public ExpirableEvent createRequestFromPayload(Payload payload) {
        return new ExpirableEvent(intGenerator.getNext().toString(),
                this.pingExpiresInSec,payload);
    }

    @Override
    public void onMessage(Payload data) {

    }

    @Override
    public void sendMessage(Payload data) {

    }

    @Override
    public void onServiceDown(Payload data) {

    }

    @Override
    public void onServiceUp(Payload data) {

    }
}

