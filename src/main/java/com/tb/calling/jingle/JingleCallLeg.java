package com.tb.calling.jingle;

import com.tb.calling.AbstractCallLeg;
import com.tb.calling.jingle.msgTemplates.Accept;
import com.tb.calling.jingle.msgTemplates.Proceed;
import com.tb.calling.jingle.msgTemplates.SDP;
import com.tb.common.ServiceEnum.PayloadType;
import com.tb.common.ServiceEnum.TransportPacket;
import com.tb.common.UUIDGen;
import com.tb.common.eventDriven.Connector;
import com.tb.common.eventDriven.Payload;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JingleCallLeg extends AbstractCallLeg {
    public JingleCallLeg(Connector connector, String uniqueId, String aParty, String bParty) {
        super(connector, uniqueId, aParty, bParty);
        connector.addListener(this);
    }
    @Override
    public void startCall() {
        /*System.out.println("sending Invite...");
        this.setUniqueId(UUID.randomUUID().toString());
        String data =StartCall.createMessage("1001",this.getUniqueId(),connector.getSessionId(),100);
        connector.sendTransportMessage(new Payload(this.getUniqueId(),data, VertoPacket.Invite));*/
    }
    public void modifyCall() {
        /*System.out.println("Sending Modify...");
        String data = ModifyCall.createMessage("1001",this.getUniqueId(),connector.getSessionId(),120);
        connector.sendTransportMessage(new Payload(this.getUniqueId(),data, VertoPacket.Modify));*/
    }
    @Override
    public void onStart(Object message) {

    }

    @Override
    public void onNewMessage(Object message) throws IOException {

    }

    @Override
    public void startSession(Object message) {

    }

    @Override
    public void startSession() {

    }

    @Override
    public void updateSession() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void onRing() {

    }

    @Override
    public void onAnswer() {

    }

    @Override
    public void startRing() {

    }

    @Override
    public void answer() {

    }

    @Override
    public String extractSdp(String sdp) {
        return null;
    }

    @Override
    public String extractSdpIpAndPort(String sdp) {
        return null;
    }

    @Override
    public void onTransportOpen(Payload payload) {

    }

    @Override
    public void onTransportClose(Payload payload) {

    }

    @Override
    public void onTransportMessage(Payload data) {
        String msg = data.getData();

        if (msg.contains("jm-propose")) {
            // Find the positions of the relevant substrings
            int startIndex = msg.indexOf("id=jm-propose-") + "id=jm-propose-".length();
            int endIndex = msg.indexOf(",type=chat");

            // Extract the ID from the message
            String extractedId = msg.substring(startIndex, endIndex);
            System.out.println(extractedId);

            // Call Accept class and pass extractedId
            String accept= Accept.createMessage( "test6@localhost/Conversations.ciMG", "test6@localhost", extractedId);
            Payload p= new Payload(UUIDGen.getNextAsStr(),accept, TransportPacket.Payload);
            p.getMetadata().put("useRest", true);
             this.getConnector().sendMsgToTransport(p);
            System.out.println(accept);
            // Call Proceed class and pass extractedId
            Proceed proceed = new Proceed();
            proceed.createMessage("192.168.0.31", "test5@localhost/Conversations.Vmyt", "test5@localhost/Conversations.Vmyt", extractedId);
            System.out.println(proceed);
            // Print the message for debugging
            //System.out.println("Extracted ID: " + extractedId);
            //System.out.println(msg);
        }
    }

    @Override
    public void onTransportError(Payload payload) {

    }

    @Override
    public void onTransportStatus(Payload payload) {

    }

}
