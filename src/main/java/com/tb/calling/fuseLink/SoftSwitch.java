package com.tb.calling.fuseLink;
import com.tb.calling.base.AbstractCallLeg;
import com.tb.calling.jingle.JingleStack;
import com.tb.calling.jingle.VertoStack;
import com.tb.calling.verto.VertoSettings;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallSignalingProtocol;
import com.tb.common.eventDriven.RequestAndResponse.Enums.TransportProtocol;
import com.tb.transport.rest.RestSettings;
import com.tb.transport.xmpp.XmppSettings;

import java.util.Map;

public class SoftSwitch {
    private final JingleStack jingleStack;
    private final VertoStack vertoStack;
    private final Map<String, AbstractCallLeg>

    public SoftSwitch(RestSettings restSettings,
                      XmppSettings xmppSettings,
                      VertoSettings vertoSettings) {
        this.jingleStack = new JingleStack(restSettings,xmppSettings);
        this.vertoStack= new VertoStack(vertoSettings);
    }

    public JingleStack getJingleStack() {
        return jingleStack;
    }
}