package com.tb.webSocket;

import java.net.http.WebSocket;

public class WsOnCloseData {
    public WebSocket webSocket;
    public int statusCode;
    public String reason;

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReason() {
        return reason;
    }

    public WsOnCloseData(WebSocket webSocket, int statusCode, String reason) {
        this.webSocket = webSocket;
        this.statusCode = statusCode;
        this.reason = reason;
    }
}
