package com.tb.xmpp;

import com.tb.common.eventDriven.ServicePingParams;
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
    public ServicePingParams getKeepAliveParams() {
        return servicePingParams;
    }
}
