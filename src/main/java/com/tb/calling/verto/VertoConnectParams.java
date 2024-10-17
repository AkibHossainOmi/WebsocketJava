package com.tb.calling.verto;

import com.tb.common.eventDriven.ServicePingParams;
import com.tb.transport.websocket.WebSocketSettings;

public class VertoConnectParams {
    String login;
    String password;
    WebSocketSettings webSocketSettings;
    int heartbitIntervalSec =1;
    public ServicePingParams getKeepAliveParams() {
        return servicePingParams;
    }

    ServicePingParams servicePingParams =null;
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
                              WebSocketSettings webSocketSettings, ServicePingParams servicePingParams) {
        this.login = login;
        this.password = password;
        this.webSocketSettings = webSocketSettings;
        this.servicePingParams = servicePingParams;
    }
}
