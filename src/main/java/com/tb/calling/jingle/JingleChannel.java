package com.tb.calling.jingle;
import com.tb.calling.base.Channel;
import com.tb.calling.base.SignalingListener;
import com.tb.calling.jingle.message.signaling.JingleICE;
import com.tb.calling.jingle.message.signaling.JingleSDP;
import com.tb.calling.jingle.message.signaling.Propose;
import com.tb.common.AbstractSignalingEvent;
import com.tb.common.SignalingEvent;
import com.tb.common.eventDriven.RequestAndResponse.Enums.SignalingProtocol;
import com.tb.common.eventDriven.RequestAndResponse.MultiThreadedRequestHandler;
import com.tb.common.eventDriven.RequestAndResponse.Payload;
import com.tb.common.eventDriven.TransportListener;
import com.tb.transport.rest.RestSettings;
import com.tb.transport.xmpp.XmppSettings;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class JingleChannel implements TransportListener, Channel {
    private final RestSettings restSettings;
    private final XmppSettings xmppSettings;
    private final JingleConnector jingleConnector;
    private final List<SignalingListener> publicListeners= new CopyOnWriteArrayList<>();
    private final MultiThreadedRequestHandler multiThreadedRequestHandler;
    public JingleChannel(JingleConnector jingleConnector, RestSettings restSettings, XmppSettings xmppSettings) {
        this.jingleConnector = jingleConnector;
        this.restSettings=restSettings;
        this.xmppSettings=xmppSettings;
        this.jingleConnector.addPublicListener(this);
        this.multiThreadedRequestHandler =
                new MultiThreadedRequestHandler(jingleConnector.restTransport);
    }
    @Override
    public void onTransportMessage(Payload payload) {
        String msg = payload.getData();
        AbstractSignalingEvent signalingMessage=null;
        if (msg.contains("jm-propose")) {
            signalingMessage = new Propose(msg, true);
        }
        if (msg.contains("session-initiate")) {
            signalingMessage = new JingleSDP(msg, true);
        }
        if (msg.contains("transport-info")) {//on ice
            signalingMessage= new JingleICE(msg, true);
        }
        if (signalingMessage!=null){
            //notify stacks
            for (SignalingListener publicListener : this.publicListeners) {
                publicListener.onSignalingMessage(signalingMessage);
            }
        }
    }
    @Override
    public void sendSignalingMessage(SignalingEvent msg) {

    }

    @Override
    public void addPublicListener(SignalingListener listener) {
        this.publicListeners.add(listener);
    }

    @Override
    public SignalingProtocol getSignalingProtocol() {
        return SignalingProtocol.JINGLE;
    }

    @Override
    public void onTransportError(Payload payload) {

    }
    @Override
    public void onTransportStatus(Payload payload) {

    }
    @Override
    public void onTransportOpen(Payload payload) {

    }
    @Override
    public void onTransportClose(Payload payload) {

    }
    public MultiThreadedRequestHandler getMultiThreadedRequestHandler() {
        return multiThreadedRequestHandler;
    }
}
