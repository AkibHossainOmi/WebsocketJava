package com.tb.webSocket;

import java.net.http.WebSocket;

public class WsOnOpenData {
    public WsOnOpenData(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    WebSocket webSocket;
}
