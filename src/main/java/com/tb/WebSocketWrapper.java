package com.tb;
import com.tb.common.Communicator.InternalSocket.TransportListener;
import com.tb.common.Communicator.InternalSocket.Transport;
import com.tb.webSocket.*;
import jdk.jshell.spi.ExecutionControl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebSocketWrapper implements Transport<String> {
    URI uri;
    protected WebSocket webSocket;
    WebSocketSettings settings;
    List<TransportListener> transportListeners =new CopyOnWriteArrayList<>();

    public WebSocketWrapper(WebSocketSettings settings) {
        this.settings=settings;
        this.uri = URI.create(settings.getUri());
        createWebSocketListener(transportListeners);
    }
    private WebSocket.Listener createWebSocketListener(List<TransportListener> transportListeners) {
        return new WebSocket.Listener() {
            @Override
            public void onOpen(WebSocket webSocket) {
                System.out.println("Connected to WebSocket");
                webSocket.request(1); // Request to receive the next message
            }

            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                for (TransportListener transportListener : transportListeners) {
                    transportListener.onTransportMessage(data.toString());
                }
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
    }
    public void connect(WebSocket.Listener listener) {
        switch (settings.getWebSocketType()) {
            case Wss -> {
                createWss();
            }
            case Ws -> {
                HttpClient client = HttpClient.newHttpClient();
                WebSocket.Builder builder = client.newWebSocketBuilder();
                this.webSocket = builder.buildAsync(uri, listener).join();
            }
        }
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void sendMessage(String wsSentData) {
        webSocket.sendText(wsSentData,true);
        webSocket.request(1);
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
}
