package com.tb;
import com.tb.webSocket.*;
import jdk.jshell.spi.ExecutionControl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;

public class WebSocketWrapper{
    URI uri;
    protected WebSocket webSocket;
    WebSocketSettings settings;

    public WebSocketWrapper(WebSocketSettings settings) {
        this.settings=settings;
        this.uri = URI.create(settings.getUri());
        createWebSocketListener(settings);
    }
    private void createWebSocketListener(WebSocketSettings settings) {

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
}
