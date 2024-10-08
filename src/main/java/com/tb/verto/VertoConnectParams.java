package com.tb.verto;

import com.tb.common.Communicator.KeepAliveParams;
import com.tb.webSocket.WebSocketSettings;

public class VertoConnectParams {
    String login;
    String password;
    WebSocketSettings webSocketSettings;

    public KeepAliveParams getKeepAliveParams() {
        return keepAliveParams;
    }

    KeepAliveParams keepAliveParams=null;
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public WebSocketSettings getWebSocketSettings() {
        return webSocketSettings;
    }

    public VertoConnectParams(String login, String password,
                              WebSocketSettings webSocketSettings,KeepAliveParams keepAliveParams) {
        this.login = login;
        this.password = password;
        this.webSocketSettings = webSocketSettings;
        this.keepAliveParams=keepAliveParams;
    }
}
