package com.tb.calling.jingle;

import com.tb.calling.*;
import com.tb.calling.jingle.msgTemplates.Accept;
import com.tb.calling.jingle.msgTemplates.Ice;
import com.tb.calling.jingle.msgTemplates.Proceed;
import com.tb.calling.jingle.msgTemplates.SDP;
import com.tb.calling.verto.VertoConnector;
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
        connector.addPublicListener(this);

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
        if (msg.contains("jm-propose")) {
            // Find the positions of the relevant substrings
            int startIndex = msg.indexOf("id=jm-propose-") + "id=jm-propose-".length();
            int endIndex = msg.indexOf(",type=chat");

            // Extract the ID from the message
            this.setUniqueId(msg.substring(startIndex, endIndex));
            this.accept();
            this.proceed();
        }

//            sendSdp();
//
//            sendIce();

        if(msg.contains("transport-info")){//on ice
            int portIndex = msg.indexOf("port=&apos;") + "port=&apos;".length();
            String subStr= msg.substring(portIndex);
            int port= Integer.parseInt(subStr.split("&")[0]);
            if (port<=0)
                throw new RuntimeException("Media Port must be >0 ");
            int ipIndex = msg.indexOf("ip=&apos;") + "ip=&apos;".length();
            subStr= msg.substring(ipIndex);
            String ip= subStr.split("&")[0];

            ICECandidate candidate= new ICECandidate(ip,
                    port, CandidateType.HOST, TransportProtocol.UDP);
            this.vertoCall= new VertoCallLeg(this.vertoConnector,UUIDGen.getNextAsStr(),"09646888888","01754105098");
            this.vertoCall.setRemoteIce(candidate);
            this.vertoCall.getConnector().addPublicListener(this.vertoCall);
            this.vertoCall.setJingleCall(this);
            this.vertoCall.startSession();
        }

    }

    public void sendIce() {
        String ip = this.getRemoteIce().getIpAddress();
        int port = this.getRemoteIce().getPort();
        String ice = Ice.createMessage("test5@localhost/Conversations.Ae9N",
                "test6@localhost/Conversations.9FIn",this.getUniqueId(),
                ip, port);
        Payload payload= new Payload(UUIDGen.getNextAsStr(),ice, TransportPacket.Payload);
        payload.getMetadata().put("useRest", true);
        this.getConnector().sendMsgToConnector(payload);
    }

    public void sendSdp() {
        String sdp = SDP.createMessage("test5@localhost/Conversations.Ae9N",
                "test6@localhost/Conversations.9FIn",this.getUniqueId());
        Payload s= new Payload(UUIDGen.getNextAsStr(),sdp, TransportPacket.Payload);
        s.getMetadata().put("useRest", true);
        this.getConnector().sendMsgToConnector(s);
    }

    public void proceed() {
        String proceed = Proceed.createMessage("test5@localhost/Conversations.Ae9N",
                "test6@localhost/Conversations.9FIn", this.getUniqueId());
        Payload p= new Payload(UUIDGen.getNextAsStr(),proceed, TransportPacket.Payload);
        p.getMetadata().put("useRest", true);
        this.getConnector().sendMsgToConnector(p);
    }

    public void accept() {
        // Call Accept class and pass extractedId
        String accept= Accept.createMessage( "test6@localhost/Conversations.9FIn", "test6@localhost", this.getUniqueId());
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
