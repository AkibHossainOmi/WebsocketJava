package com.tb.transport.websocket;

import com.tb.common.WebSocketType;

import java.net.URI;
import java.net.http.WebSocket;

public class WebSocketSettings {
    WebSocketType webSocketType;
    String uri;
    int requestBatchSize;
    public WebSocketType getWebSocketType() {
        return webSocketType;
    }

    public String getUri() {
        return uri;
    }

    public int getRequestBatchSize() {
        return requestBatchSize;
    }

    public WebSocketSettings(WebSocketType webSocketType, String uri, int requestBatchSize) {
        this.webSocketType = webSocketType;
        this.uri = uri;
        this.requestBatchSize = requestBatchSize;
        String typeIndicator=uri.split(":")[0];
        if(typeIndicator.equals("wss")){
            this.webSocketType=WebSocketType.Wss;
        }
        else if(typeIndicator.equals("ws")){
            this.webSocketType=WebSocketType.Ws;
        }
        else throw new ArrayIndexOutOfBoundsException("Unknown websocket type");
    }
}
