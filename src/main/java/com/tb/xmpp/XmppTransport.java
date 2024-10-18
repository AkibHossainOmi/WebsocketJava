package com.tb.xmpp;

import com.tb.common.ServiceEnum.TransportPacket;
import com.tb.common.UUIDGen;
import com.tb.common.eventDriven.TransportListener;
import com.tb.common.eventDriven.Transport;
import com.tb.common.eventDriven.Payload;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
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

public class XmppTransport implements Transport {
    URI uri;
    XMPPTCPConnectionConfiguration xmppConfig;
    AbstractXMPPConnection connection;
    StanzaListener xmppListener;
    XmppSettings settings;
    List<TransportListener> publicListeners =new CopyOnWriteArrayList<>();
    public XmppTransport(XmppSettings settings,
                         List<TransportListener> publicListeners) {
        this.settings=settings;
        for (TransportListener publicListener : publicListeners) {
            this.publicListeners.add(publicListener);
        }
        try {
            this.xmppConfig = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword(settings.xmppLogin, settings.password)  // Update credentials as needed
                    .setXmppDomain(settings.domain)
                    .setHost(settings.hostname)  // Replace with your XMPP server IP
                    .setPort(settings.port)  // Default XMPP port
                    .setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)  // Disable SSL for local test
                    .build();
        } catch (XmppStringprepException e) {
            throw new RuntimeException(e);
        }
        this.xmppListener = createXmppListener(connection);
    }
    private StanzaListener createXmppListener(AbstractXMPPConnection connection) {
        return new StanzaListener() {
            @Override
            public void processStanza(Stanza stanza) throws SmackException.NotConnectedException, InterruptedException {
                for (TransportListener publicListener : publicListeners) {
                    publicListener.onTransportMessage(new Payload(UUID.randomUUID().toString(),
                            stanza.toString(), TransportPacket.Payload));
                }
            }
        };
    }
    public void connect() {
        this.connection = new XMPPTCPConnection(xmppConfig);
        this.connection.addAsyncStanzaListener(xmppListener, stanza -> stanza instanceof Message);
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
        } finally {
            if (connection.isConnected()) {
                connection.disconnect();
                System.out.println("Disconnected from the XMPP server.");
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
