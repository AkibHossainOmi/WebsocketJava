package com.tb.webSocket;

import java.net.http.WebSocket;

public class WsOnMsgData {
    public WsOnMsgData(WebSocket webSocket, CharSequence data, boolean last) {
        this.webSocket = webSocket;
        this.data = data;
        this.last = last;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public CharSequence getData() {
        return data;
    }

    public boolean isLast() {
        return last;
    }

    public WebSocket webSocket;
    public CharSequence data;
    public boolean last;
}
