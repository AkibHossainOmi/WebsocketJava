package com.tb;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

import static com.tb.xmpp.XmppClientTb.*;
import static com.tb.xmpp.XmppClientTb.onAccept;

public class XmppRun {
    // Build the XMPP configuration
    public void XmppInstance() throws XmppStringprepException {
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword("test101", "test123")  // Update credentials as needed
                .setXmppDomain("localhost")
                .setHost("192.168.0.31")  // Replace with your XMPP server IP
                .setPort(5222)  // Default XMPP port
                .setSecurityMode(XMPPTCPConnectionConfiguration.SecurityMode.disabled)  // Disable SSL for local test
                .build();

        // Create a connection
        AbstractXMPPConnection connection = new XMPPTCPConnection(config);
        try {
            // Connect and log in
            connection.connect();
            System.out.println("Connected to XMPP server.");
            connection.login();
            System.out.println("Logged in as " + connection.getUser());

            // Set presence to available
            Presence presence = new Presence(Presence.Type.available);
            presence.setMode(Presence.Mode.available);
            presence.setStatus("Ready for calls");
            connection.sendStanza(presence);

            // Set up a listener to handle incoming messages
            connection.addAsyncStanzaListener(stanza -> {
                if (stanza instanceof Message) {
                    Message receivedMessage = (Message) stanza;
//                    receivedMessage = ConstructMessageTest.ringingMessage("of5@telcohost/Conversations.qxbZ", "tx51W3rRMJjXy-Ial-4l5A");
                    System.out.println("Received message: " + receivedMessage.toXML());
//                    String element = determineElement(receivedMessage);
//                    switch (element){
//                        case "propose":
//                            onPropose(connection, receivedMessage);
//                            break;
//                        case "ringing":
//                            onRinging(connection, receivedMessage);
//                            break;
//                        case "accept":
//                            onAccept(connection, receivedMessage);
//                            break;
//                    }
                }
            }, stanza -> stanza instanceof Message);

            // Keep the connection alive
            Thread.sleep(Long.MAX_VALUE);

        } catch (SmackException | IOException | InterruptedException | XMPPException e) {
            e.printStackTrace();
        } finally {
            if (connection.isConnected()) {
                connection.disconnect();
                System.out.println("Disconnected from the XMPP server.");
            }
        }
    }
}
