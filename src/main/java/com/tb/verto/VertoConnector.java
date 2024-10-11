package com.tb.verto;

import com.tb.WebSocketWrapper;
import com.tb.common.Communicator.Connector;
import com.tb.common.eventDriven.ExpirableEvent;
import com.tb.common.eventDriven.ServiceHealthUtil;
import com.tb.common.eventDriven.ServiceHealthMonitor;
import com.tb.verto.msgTemplates.*;
import com.tb.webSocket.*;

import java.net.http.WebSocket;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

class VertoConnector implements Connector, ServiceHealthUtil {
    //Transport webSocket;
    VertoConnectParams params;
    private String callId;
    private String sessionId;
    VertoPingUtil pingFactory;
    ServiceHealthMonitor serviceHealthMonitor;
    WebSocketWrapper transport;
    WebSocket.Listener webSocetListener;
    public VertoConnector(VertoConnectParams params) {
        this.params=params;
        Random random = new Random();
        this.transport = new WebSocketWrapper(params.getWebSocketSettings());
        this.transport.connect(createWsListener(this));
        login();
        //ping();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendCall();
        this.serviceHealthMonitor = new ServiceHealthMonitor(web params.servicePingParams);
        sendHangup();
        this.pingFactory= new VertoPingUtil();

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
    //@Override
    protected void startReconnectScheduler() {

    }
    public void checkConnectionState() {
    }
    private WebSocket.Listener createWsListener(Connector connector) {
        WebSocket.Listener listener = new WebSocket.Listener() {
            @Override
            public void onOpen(WebSocket webSocket) {
                System.out.println("Connected to WebSocket");
                webSocket.request(1); // Request to receive the next message
            }

            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                System.out.println("Received message from websocket: " + data);
                connector.onMessage(new WsOnMsgData(webSocket, data, last));
                webSocket.request(1); // Request to receive the next message
                return null;
            }

            @Override
            public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                System.out.println("WebSocket closed: " + statusCode + " - " + reason);
                return null;
            }

            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                System.out.println("Websocket Error: " + error.getMessage());
            }
        };
        return listener;
    }

    @Override
    public void onMessage(Object o) {

    }

    @Override
    public void sendMessage(Object o) {

    }

    @Override
    public void onServicedown(String data) {

    }

    @Override
    public void onServiceUp(String data) {

    }

    @Override
    public ExpirableEvent createServicePingMsg() {
        return null;
    }
    @Override
    public ExpirableEvent createKeepAliveMsg() {
        return null;
    }


}

