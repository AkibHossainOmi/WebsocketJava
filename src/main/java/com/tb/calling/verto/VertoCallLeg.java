package com.tb.calling.verto;

import com.tb.calling.base.AbstractCallLeg;
import com.tb.calling.base.ICECandidate;
import com.tb.calling.jingle.JingleCallLeg;
import com.tb.common.eventDriven.RequestAndResponse.Enums.*;
import com.tb.common.eventDriven.Connector;
import com.tb.calling.verto.msgTemplates.StartCall;
import com.tb.common.uniqueIdGenerator.ShortIdGenerator;
import com.tb.common.uniqueIdGenerator.UniqueIntGenerator;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.tb.common.eventDriven.RequestAndResponse.Payload;
public class VertoCallLeg extends AbstractCallLeg {
    UniqueIntGenerator intGenerator= new UniqueIntGenerator(0);

    public JingleCallLeg getJingleLeg() {
        return jingleLeg;
    }

    public void setJingleLeg(JingleCallLeg jingleLeg) {
        this.jingleLeg = jingleLeg;
    }

    JingleCallLeg jingleLeg;
    public VertoCallLeg(Connector connector) {
        super(connector);
    }
    @Override
    public void onStart(Object message) {

    }

    @Override
    public void onNewMessage(Object message) throws IOException {

    }
    @Override
    public void startSession() {
        System.out.println("sending Invite...");
        this.setSessionId(UUID.randomUUID().toString());
        ICECandidate firstCandidate= null;
        Map.Entry<String, ICECandidate> firstEntry= this.remoteIceCandidates.entrySet().iterator().next();
        firstCandidate=firstEntry.getValue();
        String msg =StartCall.createMessage("09646888888",this.getSessionId(),connector.getSessionId(),intGenerator.getNext()
        ,firstCandidate.getIpAddress(),firstCandidate.getPort());
        connector.sendMsgToConnector(new Payload(this.getSessionId(),msg, VertoPacket.Invite));
        System.out.println(msg);
        this.callState= CallState.SESSION_START;
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
            throw new RuntimeException("IP or Port not found in SDP");
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
        CallEventType callEventType = getCallMsgType(msg);
        if(callEventType !=null){
            switch (callEventType){
                case TRYING -> {}
                case RINGING -> {
                    if (this.callState==CallState.SESSION_START){
                        this.callState=CallState.RINGING;
                    }
                    String ipPort=extractSdpIpAndPort(msg);
                    String[] tempArr=ipPort.split(":");
                    int port = Integer.parseInt(tempArr[1]);
                    if (port<=0)
                        throw new RuntimeException("Media Port must be >0 ");
                    ICECandidate candidate1= new ICECandidate(ShortIdGenerator.getNext(), tempArr[0],
                            port, CandidateType.HOST, TransportProtocol.UDP);
                    ICECandidate candidate2= new ICECandidate(ShortIdGenerator.getNext(), tempArr[0],
                            port-1,CandidateType.HOST,TransportProtocol.UDP);
                    if (this.callState==CallState.RINGING) {
                        this.callState=CallState.RINGING;
                        this.jingleLeg.sendSdp();
                        this.jingleLeg.addRemoteIceCandidate(candidate1);
                        this.jingleLeg.addRemoteIceCandidate(candidate2);
                        this.jingleLeg.sendIceCandidates();
                        this.jingleLeg.sendJingleIceResults();
                    }
                }
                case ANSWER -> {}
                case HANGUP -> {}
                default -> {

                }
            }
        }

    }

    @Override
    public void onTransportError(Payload payload) {

    }

    @Override
    public void onTransportStatus(Payload payload) {

    }

    public CallEventType getCallMsgType(String msg){
        if(msg.contains("CALL CREATED")){
            return CallEventType.TRYING;
        }
        else if(msg.contains("verto.media") ){
            return CallEventType.RINGING;
        }
        else if(msg.contains("verto.answer")){
            return CallEventType.ANSWER;
        }
        else if(msg.contains("verto.bye")){
            return CallEventType.HANGUP;
        }
        else return null;
    }

}
