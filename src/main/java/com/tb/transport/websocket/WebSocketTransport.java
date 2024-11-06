package com.tb.transport.websocket;
import com.tb.common.eventDriven.RequestAndResponse.Enums.TransportPacket;
import com.tb.common.eventDriven.TransportListener;
import com.tb.transport.Transport;

import jdk.jshell.spi.ExecutionControl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import com.tb.common.eventDriven.RequestAndResponse.Payload;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebSocketTransport implements Transport {
    URI uri;
    protected WebSocketClient webSocketClient;
    WebSocketSettings settings;
    List<TransportListener> publicListeners =new CopyOnWriteArrayList<>();
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public WebSocketTransport(WebSocketSettings settings,
                              List<TransportListener> publicListeners) {
        this.settings=settings;
        this.uri = URI.create(settings.getUri());
        for (TransportListener publicListener : publicListeners) {
            this.publicListeners.add(publicListener);
        }
        this.webSocketClient= createWebSocketClient(publicListeners, this);
        // Start sending pings every 20 seconds
       /* scheduler.scheduleAtFixedRate(() -> {
            if (this.webSocketClient.isOpen()) {
                this.webSocketClient.sendPing();
            }
        }, 0, 20, TimeUnit.SECONDS);*/

    }
    void reconnect(){
        this.webSocketClient= createWebSocketClient(publicListeners, this);
        this.webSocketClient.connect();
    }
    private WebSocketClient createWebSocketClient(List<TransportListener> publicListeners,
                                                  WebSocketTransport myself) {
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

                if (!remote){
                    //myself.reconnect();
                    for (TransportListener publicListener : publicListeners) {
                        publicListener.onTransportMessage(new Payload(UUID.randomUUID().toString(),
                                "reconnect", TransportPacket.Payload));
                    }
                }
            }

            @Override
            public void onError(Exception ex) {
                System.out.printf(ex.toString());
            }
        };
    }
    @Override
    public void connectOrInit() {
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
