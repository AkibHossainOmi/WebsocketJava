package com.tb.transport.websocket;
import com.tb.common.ServiceEnum.TransportPacket;
import com.tb.common.eventDriven.TransportListener;
import com.tb.common.eventDriven.Transport;
import com.tb.common.eventDriven.Payload;
import jdk.jshell.spi.ExecutionControl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebSocketProxy implements Transport {
    URI uri;
    protected WebSocketClient webSocketClient;
    WebSocketSettings settings;
    List<TransportListener> publicListeners =new CopyOnWriteArrayList<>();

    public WebSocketProxy(WebSocketSettings settings,
                          List<TransportListener> publicListeners) {
        this.settings=settings;
        this.uri = URI.create(settings.getUri());
        for (TransportListener publicListener : publicListeners) {
            this.publicListeners.add(publicListener);
        }
        this.webSocketClient= createWebSocketClient(publicListeners);
    }
    private WebSocketClient createWebSocketClient(List<TransportListener> publicListeners) {
        Map<String,String> httpHeaders  = new HashMap<>();
        //httpHeaders.put("Sec-Websocket-Protocol","janus-protocol");
        return new WebSocketClient(uri,httpHeaders) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                try {
                    System.out.printf("Websocket Connected");
                    for (TransportListener publicListener : publicListeners) {
                        publicListener.onTransportOpen(
                                new Payload(UUID.randomUUID().toString(),"connected", TransportPacket.TransportUp));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String data) {
                for (TransportListener publicListener : publicListeners) {
                    publicListener.onTransportMessage(new Payload(UUID.randomUUID().toString(),
                            data.toString(), TransportPacket.Payload));
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.printf(reason);
            }

            @Override
            public void onError(Exception ex) {
                System.out.printf(ex.toString());
            }
        };
    }
    public void connect(Transport transport) {
        switch (settings.getWebSocketType()) {
            case Wss -> {
                createWss();
            }
            case Ws -> {
                this.webSocketClient.connect();
            }
        }
    }

    public WebSocketClient getWebSocket() {
        return webSocketClient;
    }

    private void createWss() {
        try {
            throw new ExecutionControl.NotImplementedException("Method createWss not implemented yet.");
        } catch (ExecutionControl.NotImplementedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addListener(TransportListener transportListener) {

    }

    @Override
    public void sendMessage(Payload payload) {
        webSocketClient.send(payload.getData());
    }

}
