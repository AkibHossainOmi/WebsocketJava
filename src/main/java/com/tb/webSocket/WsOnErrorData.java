package com.tb.webSocket;

import java.net.http.WebSocket;

public class WsOnErrorData {
    public WebSocket webSocket;
    public Throwable error;

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public Throwable getError() {
        return error;
    }

    public WsOnErrorData(WebSocket webSocket, Throwable error) {
        this.webSocket = webSocket;
        this.error = error;
    }
}
