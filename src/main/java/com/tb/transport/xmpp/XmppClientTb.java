package com.tb.transport.xmpp;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.StandardExtensionElement;

public class XmppClientTb {



    // Updated onPropose method to accept connection parameter
    public static void onPropose(AbstractXMPPConnection connection, Message message) {
        // Extract the <propose> element from the received message
        StandardExtensionElement proposeElement = message.getExtension("propose", "urn:xmpp:jingle-message:0");
        String proposeId = proposeElement.getAttributeValue("id");
        System.out.println("<propose> element is present with ID: " + proposeId);

        // Dynamically get the 'from' and 'to' values from the received message
        String fromJid = message.getFrom().asBareJid().toString();  // 'from' JID
        String toJid = message.getTo().asBareJid().toString();      // 'to' JID

        // Create the response message dynamically
        Message responseMessage = new Message();
        responseMessage.setType(Message.Type.chat);
        responseMessage.setFrom(fromJid);  // Use dynamic 'from' JID
        responseMessage.setTo(toJid);      // Use dynamic 'to' JID

        // Construct the XML string for the response dynamically
        String xmlResponse =
                "<message type=\"chat\" id=\"jm-propose-" + proposeId + "\" from=\"" + fromJid + "\" to=\"" + toJid + "\">\n" +
                        "  <propose xmlns=\"urn:xmpp:jingle-message:0\" id=\"" + proposeId + "\">\n" +
                        "    <description xmlns=\"urn:xmpp:jingle:apps:rtp:1\" media=\"audio\" />\n" +
                        "  </propose>\n" +
                        "  <request xmlns=\"urn:xmpp:receipts\" />\n" +
                        "  <store xmlns=\"urn:xmpp:hints\" />\n" +
                        "</message>\n";

        // Set the dynamically constructed XML as the body of the response message
        responseMessage.setBody(xmlResponse);

        // Send the response message
        try {
            connection.sendStanza(responseMessage);
            System.out.println("Dynamic response sent");
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void onRinging(AbstractXMPPConnection connection, Message message) {
        // Extract the <ringing> element from the received message
        StandardExtensionElement ringingElement = message.getExtension("ringing", "urn:xmpp:jingle-message:0");
        if (ringingElement != null) {
            String ringingId = ringingElement.getAttributeValue("id");
            System.out.println("<ringing> element is present with ID: " + ringingId);

            // Dynamically get the 'from' and 'to' values from the received message
            String fromJid = message.getFrom().toString();  // 'from' JID
            String toJid = message.getTo().toString();      // 'to' JID

            // Create a response message dynamically
            Message responseMessage = new Message();
            responseMessage.setType(Message.Type.chat);
            responseMessage.setFrom(fromJid);  // Use dynamic 'from' JID
            responseMessage.setTo(toJid);      // Use dynamic 'to' JID

            // Dynamically construct the forwarded part of the message
            String responseXml = "<message type=\"chat\" from=\"" + fromJid + "\" to=\"" + toJid + "\">" +
                    "<sent xmlns=\"urn:xmpp:carbons:2\">" +
                    "<forwarded xmlns=\"urn:xmpp:forward:0\">" +
                    "<message to=\"" + toJid + "\" from=\"" + fromJid + "\" xmlns=\"jabber:client\" type=\"chat\">" +
                    "<ringing xmlns=\"urn:xmpp:jingle-message:0\" id=\"" + ringingId + "\" />" +
                    "<store xmlns=\"urn:xmpp:hints\" />" +
                    "</message>" +
                    "</forwarded>" +
                    "</sent>" +
                    "</message>";

            // Set the dynamically constructed XML as the response message body
            responseMessage.setBody(responseXml);

            // Send the response message
            try {
                connection.sendStanza(responseMessage);
                System.out.println("Dynamic response sent");
            } catch (SmackException.NotConnectedException | InterruptedException e) {
                e.printStackTrace();
            }

            // Construct another message with the <ringing> info
            String responseXml2 = "<message type=\"chat\" from=\"" + fromJid + "\" to=\"" + toJid + "\">" +
                    "<ringing xmlns=\"urn:xmpp:jingle-message:0\" id=\"" + ringingId + "\" />" +
                    "<store xmlns=\"urn:xmpp:hints\" />" +
                    "</message>";

            // Create a new message for the second response and set the body
            Message secondResponseMessage = new Message();
            secondResponseMessage.setType(Message.Type.chat);
            secondResponseMessage.setFrom(fromJid);  // Use dynamic 'from' JID
            secondResponseMessage.setTo(toJid);      // Use dynamic 'to' JID
            secondResponseMessage.setBody(responseXml2);

            // Send the second response message
            try {
                connection.sendStanza(secondResponseMessage);
                System.out.println("Dynamic ringing message sent from: " + fromJid + " to: " + toJid);
            } catch (SmackException.NotConnectedException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("<ringing> element not found in the received message.");
        }
    }

    public static void onAccept(AbstractXMPPConnection connection, Message message) {
        // Extract the <accept> element from the received message
        StandardExtensionElement acceptElement = message.getExtension("accept", "urn:xmpp:jingle-message:0");
        String acceptId = acceptElement.getAttributeValue("id");
        System.out.println("<accept> element is present with ID: " + acceptId);

        // Dynamically get the 'from' and 'to' values from the received message
        String fromJid = message.getFrom().asBareJid().toString();  // 'from' JID
        String toJid = message.getTo().asBareJid().toString();      // 'to' JID

        // Construct and send Message 1
        String xmlMessage1 =
                "<message type=\"chat\" from=\"of5@telcohost\" to=\"of5@telcohost/xabber-android-E8suzyJd\">\n" +
                        "  <sent xmlns=\"urn:xmpp:carbons:2\">\n" +
                        "    <forwarded xmlns=\"urn:xmpp:forward:0\">\n" +
                        "      <message type=\"chat\" from=\"of5@telcohost/Conversations.w6p2\" xmlns=\"jabber:client\">\n" +
                        "        <accept xmlns=\"urn:xmpp:jingle-message:0\" id=\"" + acceptId + "\" />\n" +
                        "        <store xmlns=\"urn:xmpp:hints\" />\n" +
                        "      </message>\n" +
                        "    </forwarded>\n" +
                        "  </sent>\n" +
                        "</message>";
        Message message1 = new Message();
        message1.setType(Message.Type.chat);
        message1.setFrom("of5@telcohost");
        message1.setTo("of5@telcohost/xabber-android-E8suzyJd");
        message1.setBody(xmlMessage1);
        try {
            connection.sendStanza(message1);
            System.out.println("Sent message 1");
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }

        // Construct and send Message 2
        String xmlMessage2 =
                "<message to=\"of5@telcohost\" from=\"of5@telcohost/Conversations.w6p2\" type=\"chat\">\n" +
                        "  <accept xmlns=\"urn:xmpp:jingle-message:0\" id=\"" + acceptId + "\" />\n" +
                        "  <store xmlns=\"urn:xmpp:hints\" />\n" +
                        "</message>";
        Message message2 = new Message();
        message2.setType(Message.Type.chat);
        message2.setFrom("of5@telcohost/Conversations.w6p2");
        message2.setTo("of5@telcohost");
        message2.setBody(xmlMessage2);
        try {
            connection.sendStanza(message2);
            System.out.println("Sent message 2");
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }

        // Construct and send Message 3
        String xmlMessage3 =
                "<message to=\"of5@telcohost\" from=\"of5@telcohost/Conversations.w6p2\" type=\"chat\">\n" +
                        "  <accept xmlns=\"urn:xmpp:jingle-message:0\" id=\"" + acceptId + "\" />\n" +
                        "  <store xmlns=\"urn:xmpp:hints\" />\n" +
                        "</message>";
        Message message3 = new Message();
        message3.setType(Message.Type.chat);
        message3.setFrom("of5@telcohost/Conversations.w6p2");
        message3.setTo("of5@telcohost");
        message3.setBody(xmlMessage3);
        try {
            connection.sendStanza(message3);
            //connection.
            System.out.println("Sent message 3");
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void sendIqPacket(){

    }


    public static String determineElement(Message message) {
        if (message.getExtension("propose", "urn:xmpp:jingle-message:0") != null) {
            return "propose";
        } else if (message.getExtension("ringing", "urn:xmpp:jingle-message:0") != null) {
            return "ringing";
        } else if (message.getExtension("accept", "urn:xmpp:jingle-message:0") != null) {
            return "accept";
        } else {
            return "unknown";
        }
    }


    public Object getApplicationContext() {
        return null;
    }
}
