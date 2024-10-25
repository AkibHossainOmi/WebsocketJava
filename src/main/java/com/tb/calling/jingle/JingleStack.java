package com.tb.calling.jingle;

import com.tb.calling.AbstractCallLeg;
import com.tb.transport.rest.RestSettings;
import com.tb.transport.xmpp.XmppSettings;
import org.jivesoftware.smack.ConnectionConfiguration;

import java.util.Hashtable;

public class JingleStack {

    private final Hashtable<String, AbstractCallLeg> jingleCalls;
    private final JingleChannelDriver jingleChannelDriver;
    private final JingleMessageNormalizer messageNormalizer;
    private final JingleConnector jingleConnector;
    private final RestSettings restSettings;
    private final XmppSettings xmppSettings;
    public JingleStack(RestSettings restSettings,
                       XmppSettings xmppSettings) {
        this.jingleCalls = new Hashtable<>();
        this.restSettings= restSettings;
        this.xmppSettings= xmppSettings;
        this.jingleConnector= new JingleConnector(xmppSettings,restSettings);
        jingleConnector.connectOrInit();
        this.jingleChannelDriver = new JingleChannelDriver(jingleConnector);
        this.messageNormalizer = new JingleMessageNormalizer(jingleChannelDriver, jingleConnector);
    }

    public void handleIncomingRawMessage(String rawMessage) {
        // Delegates raw message processing to the message normalizer
        messageNormalizer.handleIncomingMessage(rawMessage);
    }

    public void addCall(AbstractCallLeg call) {
        jingleCalls.put(call.getUniqueId(), call);
    }

    public void removeCall(String callId) {
        jingleCalls.remove(callId);
    }

    public AbstractCallLeg getCall(String callId) {
        return jingleCalls.get(callId);
    }

    public Hashtable<String, AbstractCallLeg> getJingleCalls() {
        return jingleCalls;
    }
}
