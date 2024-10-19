package com.tb.transport.xmpp;

import com.tb.common.eventDriven.ServicePingParams;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class XmppSettings {
    public String hostname;
    public int port;
    public String xmppLogin;
    public String password;
    public String domain;
    public XMPPTCPConnectionConfiguration.SecurityMode securityMode;
    public int heartbitIntervalSec =1;
    public ServicePingParams servicePingParams =null;

    public XmppSettings(String hostname, int port, String xmppLogin, String password, String domain, XMPPTCPConnectionConfiguration.SecurityMode securityMode, int heartbitIntervalSec) {
        this.hostname = hostname;
        this.port = port;
        this.xmppLogin = xmppLogin;
        this.password = password;
        this.domain = domain;
        this.securityMode = securityMode;
        this.heartbitIntervalSec = heartbitIntervalSec;
    }

    public ServicePingParams getKeepAliveParams() {
        return servicePingParams;
    }
}
