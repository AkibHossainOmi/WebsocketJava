package com.tb.calling;

import com.tb.common.ServiceEnum.VertoPacket;
import com.tb.common.eventDriven.Connector;
import com.tb.common.eventDriven.Payload;
import com.tb.calling.verto.msgTemplates.ModifyCall;
import com.tb.calling.verto.msgTemplates.StartCall;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VertoCallLeg extends AbstractCallLeg {
    @Override
    public void startCall() {
        System.out.println("sending Invite...");
        this.setUniqueId(UUID.randomUUID().toString());
        String data =StartCall.createMessage("1001",this.getUniqueId(),connector.getSessionId(),100);
        connector.sendTransportMessage(new Payload(this.getUniqueId(),data, VertoPacket.Invite));
    }
    public void modifyCall() {
        System.out.println("Sending Modify...");
        String data = ModifyCall.createMessage("1001",this.getUniqueId(),connector.getSessionId(),120);
        connector.sendTransportMessage(new Payload(this.getUniqueId(),data, VertoPacket.Modify));
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
        return null;//implement here
    }

    @Override
    public String extractSdpIpAndPort(String sdp) {
        String ip = null;
        String port = null;

        if (sdp != null && !sdp.isEmpty()) {
            Pattern ipPattern = Pattern.compile("c=IN IP4 (\\d+\\.\\d+\\.\\d+\\.\\d+)");
            Matcher ipMatcher = ipPattern.matcher(sdp);
            if (ipMatcher.find()) {
                ip = ipMatcher.group(1);
            }

            Pattern portPattern = Pattern.compile("m=audio (\\d+)");
            Matcher portMatcher = portPattern.matcher(sdp);
            if (portMatcher.find()) {
                port = portMatcher.group(1);
            }
        }

        if (ip != null && port != null) {
            return String.format(ip + ":" + port);
        } else {
            return "IP or Port not found in SDP";
        }
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
        switch (getCallMsgType(msg)){
            case TRYING -> {}
            case RINGING -> extractSdpIpAndPort(msg);
            case ANSWERED -> {}
            case HANGUP -> {}
        }

    }

    @Override
    public void onTransportError(Payload payload) {

    }

    @Override
    public void onTransportStatus(Payload payload) {

    }

    public CallMsgType getCallMsgType(String msg){
        if(msg.contains("CALL CREATED")){
            return CallMsgType.TRYING;
        }
        else if(msg.contains("verto.media") ){
            return CallMsgType.RINGING;
        }
        else if(msg.contains("verto.answer")){
            return CallMsgType.ANSWERED;
        }
        else if(msg.contains("verto.bye")){
            return CallMsgType.HANGUP;
        }
        else return null;
    }
    public VertoCallLeg(Connector connector, String uniqueId, String aParty, String bParty) {
        super(connector, uniqueId, aParty, bParty);
    }
}
