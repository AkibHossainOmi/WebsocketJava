package com.tb.calling;

import com.tb.common.eventDriven.Connector;
import com.tb.common.eventDriven.Payload;

import java.io.IOException;

public class JingleCallLeg extends AbstractCallLeg {
    public JingleCallLeg(Connector connector, String uniqueId, String aParty, String bParty) {
        super(connector, uniqueId, aParty, bParty);
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
    }

    @Override
    public void onTransportError(Payload payload) {

    }

    @Override
    public void onTransportStatus(Payload payload) {

    }

}
