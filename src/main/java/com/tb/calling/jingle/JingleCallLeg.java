package com.tb.calling.jingle;

import com.tb.calling.AbstractCallLeg;
import com.tb.calling.VertoCallLeg;
import com.tb.calling.jingle.msgTemplates.Accept;
import com.tb.calling.jingle.msgTemplates.Ice;
import com.tb.calling.jingle.msgTemplates.Proceed;
import com.tb.calling.jingle.msgTemplates.SDP;
import com.tb.calling.verto.VertoConnector;
import com.tb.common.Delay;
import com.tb.common.ServiceEnum.TransportPacket;
import com.tb.common.UUIDGen;
import com.tb.common.eventDriven.Connector;
import com.tb.common.eventDriven.Payload;

import java.io.IOException;

public class JingleCallLeg extends AbstractCallLeg {
    VertoCallLeg vertoCall;
    public VertoConnector getVertoConnector() {
        return vertoConnector;
    }

    public void setVertoConnector(VertoConnector vertoConnector) {
        this.vertoConnector = vertoConnector;
    }

    VertoConnector vertoConnector;
    public JingleCallLeg(Connector connector, String uniqueId, String aParty, String bParty) {
        super(connector, uniqueId, aParty, bParty);
        connector.addListener(this);

    }
    @Override
    public void onStart(Object message) {

    }

    @Override
    public void onNewMessage(Object message) throws IOException {

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
        String extractedId="";
        if (msg.contains("jm-propose")) {
            // Find the positions of the relevant substrings
            int startIndex = msg.indexOf("id=jm-propose-") + "id=jm-propose-".length();
            int endIndex = msg.indexOf(",type=chat");

            // Extract the ID from the message
            extractedId = msg.substring(startIndex, endIndex);
            this.vertoCall= new VertoCallLeg(this.vertoConnector,UUIDGen.getNextAsStr(),"09646888888","01754105098");
            this.vertoCall.startSession();
            Delay.sleep(5000);

            accept(extractedId);
            proceed(extractedId);

        }
        if (msg.contains("session-initiate")){

            String sidStart = "sid=&apos;";
            String sidEnd = "&apos; action";

            // Find the start and end index of 'sid'
            int startIndex = msg.indexOf(sidStart) + sidStart.length();
            int endIndex = msg.indexOf(sidEnd, startIndex);

            // Extract the SID from the message
            String extractedSid = msg.substring(startIndex, endIndex);

            System.out.println(extractedSid);
            String sdp = SDP.createMessage("test5@localhost/Conversations.Ae9N","test6@localhost/Conversations.9FIn",extractedSid);
            Payload s= new Payload(UUIDGen.getNextAsStr(),sdp, TransportPacket.Payload);
            s.getMetadata().put("useRest", true);
            this.getConnector().sendMsgToConnector(s);

            String ice = Ice.createMessage("test5@localhost/Conversations.Ae9N","test6@localhost/Conversations.9FIn",extractedSid,"192.168.0.126");
            Payload i= new Payload(UUIDGen.getNextAsStr(),ice, TransportPacket.Payload);
            i.getMetadata().put("useRest", true);
            this.getConnector().sendMsgToConnector(i);
        }
    }

    private void proceed(String extractedId) {
        String proceed = Proceed.createMessage("test5@localhost/Conversations.Ae9N", "test6@localhost/Conversations.9FIn", extractedId);
        Payload p= new Payload(UUIDGen.getNextAsStr(),proceed, TransportPacket.Payload);
        p.getMetadata().put("useRest", true);
        this.getConnector().sendMsgToConnector(p);
    }

    private void accept(String extractedId) {
        // Call Accept class and pass extractedId
        String accept= Accept.createMessage( "test6@localhost/Conversations.9FIn", "test6@localhost", extractedId);
        Payload p= new Payload(UUIDGen.getNextAsStr(),accept, TransportPacket.Payload);
        p.getMetadata().put("useRest", true);
        this.getConnector().sendMsgToConnector(p);
    }

    @Override
    public void onTransportError(Payload payload) {

    }

    @Override
    public void onTransportStatus(Payload payload) {

    }

}
