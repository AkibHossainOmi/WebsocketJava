package com.tb.transport.xmpp;

import com.tb.calling.AbstractCallLeg;
import com.tb.common.StringUtil;
import com.tb.common.eventDriven.RequestAndResponse.Enums.TransportPacket;
import com.tb.common.UUIDGen;
import com.tb.common.eventDriven.TransportListener;
import com.tb.transport.Transport;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.iqrequest.AbstractIqRequestHandler;
import org.jivesoftware.smack.iqrequest.IQRequestHandler;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import com.tb.common.eventDriven.RequestAndResponse.Payload;


public class XmppTransport implements Transport {
    URI uri;
    XMPPTCPConnectionConfiguration xmppConfig;
    AbstractXMPPConnection connection;
    StanzaListener xmppListener;
    IQRequestHandler iqListener;

    XmppSettings settings;
    List<TransportListener> publicListeners = new CopyOnWriteArrayList<>();

    public XmppTransport(XmppSettings settings,
                         List<TransportListener> publicListeners) {
        this.settings = settings;
        for (TransportListener publicListener : publicListeners) {
            this.publicListeners.add(publicListener);
        }
        try {
            this.xmppConfig = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(settings.xmppLogin, settings.password)  // Update credentials as needed
                    .setXmppDomain(settings.domain)
                    .setHost(settings.hostname)  // Replace with your XMPP server IP
                    .setPort(settings.port)  // Default XMPP port
                    .setResource(settings.deviceId)
                    .setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)  // Disable SSL for local test
                    .build();
        } catch (XmppStringprepException e) {
            throw new RuntimeException(e);
        }
        this.xmppListener = createXmppListener(connection);
        this.iqListener = createIqListener(this.connection);
    }

    private StanzaListener createXmppListener(AbstractXMPPConnection connection) {
        return new StanzaListener() {
            @Override
            public void processStanza(Stanza stanza) throws SmackException.NotConnectedException, InterruptedException {
                for (TransportListener publicListener : publicListeners) {
                    publicListener.onTransportMessage(new Payload(UUID.randomUUID().toString(),
                            stanza.toXML().toString(), TransportPacket.Payload));


                }
            }
        };
    }

    private IQRequestHandler createIqListener(AbstractXMPPConnection connection) {
        return new AbstractIqRequestHandler("jingle", "urn:xmpp:jingle:1", IQ.Type.set, IQRequestHandler.Mode.sync) {
            @Override
            public IQ handleIQRequest(IQ iqRequest) {
                // Handle the incoming IQ request
                System.out.println("Received IQ: " + iqRequest.toXML());


                for (TransportListener publicListener : publicListeners) {
                    publicListener.onTransportMessage(new Payload(UUID.randomUUID().toString(),
                            iqRequest.toXML().toString(), TransportPacket.Payload));
                }


                // You can generate an appropriate IQ response here
                //IQ resultIQ = IQ.createResultIQ(iqRequest); // Creates an IQ result reply
                // Or return an error if necessary
                // IQ errorIQ = IQ.createErrorResponse(iqRequest, StanzaError.from(Condition.bad_request, "Invalid IQ request"));
                //return resultIQ;
                return null;
            }
        };
    }

    @Override
    public void connectOrInit() {
        this.connection = new XMPPTCPConnection(xmppConfig);
        this.connection.addAsyncStanzaListener(xmppListener, stanza -> stanza instanceof Message);
        this.connection.registerIQRequestHandler(this.createIqListener(this.connection));
        try {
            // Connect and log in
            this.connection.connect();
            for (TransportListener listener : this.publicListeners) {
                listener.onTransportOpen(new Payload(UUIDGen.getNextAsStr(), "Connected to XMPP Server.",
                        TransportPacket.TransportUp));
            }
            connection.login();
            System.out.println("Logged in as " + connection.getUser());
            Presence presence = new Presence(Presence.Type.available);
            presence.setMode(Presence.Mode.available);
            presence.setStatus(Presence.Type.available.toString());
            connection.sendStanza(presence);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                throw e;
            } catch (SmackException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (XMPPException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void addListener(TransportListener transportListener) {

    }

    @Override
    public void sendMessage(Payload payload) {
        //convert string to stanza here and send, not implemented yet
        throw new RuntimeException("Method not implemented yet");
    }

}
