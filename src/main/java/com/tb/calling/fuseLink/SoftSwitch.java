package com.tb.calling.fuseLink;
import com.tb.calling.base.AbstractCallLeg;
import com.tb.calling.base.TwoWayCallBridge;
import com.tb.calling.jingle.JingleChannel;
import com.tb.calling.jingle.JingleStack;
import com.tb.calling.jingle.VertoStack;
import com.tb.calling.verto.VertoCallLeg;
import com.tb.calling.verto.VertoChannel;
import com.tb.calling.verto.VertoSettings;
import com.tb.common.UUIDGen;
import com.tb.transport.rest.RestSettings;
import com.tb.transport.xmpp.XmppSettings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SoftSwitch {
    private final JingleStack jingleStack;
    private final VertoStack vertoStack;
    private final Map<String, TwoWayCallBridge> vertoLegWiseBridges = new ConcurrentHashMap<>();
    private final Map<String, TwoWayCallBridge> jingleLegWiseBridges = new ConcurrentHashMap<>();

    public SoftSwitch(RestSettings restSettings,
                      XmppSettings xmppSettings,
                      VertoSettings vertoSettings,
                      JingleChannel jingleChannel,
                      VertoChannel vertoChannel) {
        this.jingleStack = new JingleStack(restSettings,xmppSettings,jingleChannel);
        this.vertoStack= new VertoStack(vertoSettings,vertoChannel);
    }

    public JingleStack getJingleStack() {
        return jingleStack;
    }
    public void onIncomingCall(AbstractCallLeg origLeg){
        routeCall(origLeg);
    }
    private void routeCall(AbstractCallLeg origLeg){
        VertoCallLeg termLeg = new VertoCallLeg(this.vertoStack);
        termLeg.setSessionId(UUIDGen.getNextAsStr());
        termLeg.setaParty("09646888888");
        termLeg.setbParty("8801754105098");
        termLeg.getConnector().addPublicListener(this.vertoCall);
        termLeg.setJingleLeg(this);
        termLeg.startSession();

        TwoWayCallBridge bridge= new TwoWayCallBridge(origLeg,termLeg,this.jingleStack, this.vertoStack);
        //switch ( )
        this.jingleLegWiseBridges.put(origLeg.getSessionId(), bridge);
        this.vertoLegWiseBridges.put(termLeg.getSessionId(), bridge);
    }
}