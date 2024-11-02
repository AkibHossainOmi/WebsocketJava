package com.tb.calling.jingle;
import com.tb.calling.base.Channel;
import com.tb.calling.base.ChannelListener;
import com.tb.calling.jingle.ConversationsRequests.JingleMsgType;
import com.tb.calling.jingle.message.signaling.JingleICE;
import com.tb.calling.jingle.message.signaling.JingleSDP;
import com.tb.calling.jingle.message.signaling.Propose;
import com.tb.common.AbstractSignalingMessage;
import com.tb.common.SignalingMessage;
import com.tb.common.StringUtil;
import com.tb.common.UUIDGen;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;
import com.tb.common.eventDriven.RequestAndResponse.Payload;
import com.tb.common.eventDriven.TransportListener;
import com.tb.common.uniqueIdGenerator.ShortIdGenerator;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class JingleChannel implements TransportListener, Channel {
    private final JingleConnector jingleConnector;
    private final List<ChannelListener> publicListeners= new CopyOnWriteArrayList<>();
    public JingleChannel(JingleConnector jingleConnector) {
        this.jingleConnector = jingleConnector;
        this.jingleConnector.addPublicListener(this);
    }
    @Override
    public void onTransportMessage(Payload payload) {
        String msg = payload.getData();
        AbstractSignalingMessage signalingMessage=null;
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
            for (ChannelListener publicListener : this.publicListeners) {
                publicListener.onChannelMessage(signalingMessage);
            }
        }
    }
    @Override
    public void sendSignalingMessage(SignalingMessage msg) {

    }

    @Override
    public void addPublicListener(ChannelListener listener) {
        this.publicListeners.add(listener);
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
}
