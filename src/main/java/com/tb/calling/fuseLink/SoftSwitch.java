package com.tb.calling.fuseLink;
import java.util.Hashtable;
import java.util.Map;
import com.tb.calling.jingle.JingleStack;
import com.tb.transport.rest.RestSettings;
import com.tb.transport.xmpp.XmppSettings;

public class SoftSwitch {
    private final JingleStack jingleStack;

    public SoftSwitch(RestSettings restSettings,
                      XmppSettings xmppSettings) {
        this.jingleStack = new JingleStack(restSettings,xmppSettings);
    }

    public JingleStack getJingleStack() {
        return jingleStack;
    }
}