package com.tb;
import com.tb.common.Communicator.Communicator;
import com.tb.common.Communicator.KeepAliveParams;
import com.tb.common.Communicator.Transport;
import com.tb.webSocket.*;
import jdk.jshell.spi.ExecutionControl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;

public abstract class WebSocketWrapper extends Communicator {
    URI uri;
    protected WebSocket webSocket;
    WebSocketSettings settings;
    WebSocket.Listener listener;
    public WebSocketWrapper(KeepAliveParams keepAliveParams, WebSocketSettings settings) {
        //String uri,int requestBatchSize
        super(keepAliveParams);
        this.settings=settings;
        this.uri = URI.create(settings.getUri());
        createWebSocketListener(settings);
    }

    private void createWebSocketListener(WebSocketSettings settings) {
        switch (settings.getWebSocketType()) {
            case Wss -> {
                createWss();
            }
            case Ws -> {
                this.listener=createWsListener(this);
            }
        }
    }

    @Override
    public Transport connectOrReconnect() {
        HttpClient client = HttpClient.newHttpClient();
        WebSocket.Builder builder = client.newWebSocketBuilder();
        this.webSocket = builder.buildAsync(uri, listener).join();
        return this;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void sendMessage(WsSentData wsSentData) {
        webSocket.sendText(wsSentData.getData(),true);
        webSocket.request(1);
    }
    private WebSocket.Listener createWsListener(Communicator communicator) {
        WebSocket.Listener listener = new WebSocket.Listener() {
            @Override
            public void onOpen(WebSocket webSocket) {
                System.out.println("Connected to WebSocket");
                //webSocket.sendText(loginMessage, true);
                communicator.onOpen(new WsOnOpenData(webSocket));
                webSocket.request(1); // Request to receive the next message
            }

            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                System.out.println("Received message from websocket: " + data);
                communicator.onMessage(new WsOnMsgData(webSocket, data, last));
                webSocket.request(1); // Request to receive the next message
                return null;
            }

            @Override
            public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                System.out.println("WebSocket closed: " + statusCode + " - " + reason);
                communicator.onClose(new WsOnCloseData(webSocket, statusCode, reason));
                return null;
            }

            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                System.out.println("Websocket Error: " + error.getMessage());
                communicator.onError(new WsOnErrorData(webSocket, error));
            }
        };
        return listener;
    }

    private void createWss() {
        try {
            throw new ExecutionControl.NotImplementedException("Method createWss not implemented yet.");
        } catch (ExecutionControl.NotImplementedException e) {
            throw new RuntimeException(e);
        }
    }
}
