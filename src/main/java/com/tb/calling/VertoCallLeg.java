package com.tb.calling;

import com.tb.calling.jingle.JingleCallLeg;
import com.tb.calling.verto.VertoSdpParamB;
import com.tb.calling.verto.msgTemplates.Hangup;
import com.tb.common.eventDriven.RequestAndResponse.Enums.VertoPacket;
import com.tb.common.eventDriven.Connector;
import com.tb.calling.verto.msgTemplates.StartCall;
import com.tb.common.uniqueIdGenerator.ShortIdGenerator;
import com.tb.common.uniqueIdGenerator.UniqueIntGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.tb.common.eventDriven.RequestAndResponse.Payload;
public class VertoCallLeg extends AbstractCallLeg {
    UniqueIntGenerator intGenerator= new UniqueIntGenerator(0);

    public VertoSdpParamB getVertoSdpParamA() {
        return vertoSdpParamB;
    }

    private VertoSdpParamB vertoSdpParamB;

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
        this.setUniqueId(UUID.randomUUID().toString());
        ICECandidate firstCandidate= null;
        Map.Entry<String, ICECandidate> firstEntry= this.remoteIceCandidates.entrySet().iterator().next();
        firstCandidate=firstEntry.getValue();
        String msg =StartCall.createMessage("01789896378",this.getUniqueId(),connector.getSessionId(),intGenerator.getNext()
        ,firstCandidate.getIpAddress(),firstCandidate.getPort(),jingleLeg.getJingleSdpParamA().getMsid(),
                jingleLeg.getJingleSdpParamA().getUfrag(),jingleLeg.getJingleSdpParamA().getPwd(),
                jingleLeg.getJingleSdpParamA().getFingerprint(),jingleLeg.getJingleSdpParamA().getSsrc());
        connector.sendMsgToConnector(new Payload(this.getUniqueId(),msg, VertoPacket.Invite));
        System.out.println(msg);
        this.callState= CallState.SESSION_START;
    }

    public void sendHangup() {
        String msg = Hangup.createMessage(this.getUniqueId(),connector.getSessionId(),intGenerator.getNext());
        connector.sendMsgToConnector(new Payload(this.getUniqueId(),msg, VertoPacket.Hangup));
        System.out.println(msg);
        this.callState= CallState.IDLE;
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
        CallMsgType callMsgType = getCallMsgType(msg);
        if(callMsgType!=null){
            switch (callMsgType){
                case TRYING -> {}
                case RINGING, ANSWER -> {
                    if (msg.contains("msid-semantic")) {
                        try {
                            FileWriter fileWriter= new FileWriter("c:/temp/vertoSdp.txt");
                            fileWriter.write(msg.replace("\\r\\n", System.lineSeparator()));
                            fileWriter.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (this.callState==CallState.SESSION_START){
                        this.callState=CallState.RINGING;
                    }
                    this.vertoSdpParamB = new VertoSdpParamB(msg);
                    String ipPort = extractSdpIpAndPort(msg);
                    String[] tempArr=ipPort.split(":");

                    int port = Integer.parseInt(tempArr[1]);
                    if (port<=0)
                        throw new RuntimeException("Media Port must be >0 ");
                    ICECandidate candidate1= new ICECandidate(ShortIdGenerator.getNext(), tempArr[0],
                            port,CandidateType.HOST,TransportProtocol.UDP);
                    ICECandidate candidate2= new ICECandidate(ShortIdGenerator.getNext(), tempArr[0],
                            port-1,CandidateType.HOST,TransportProtocol.UDP);
                    if (this.callState==CallState.RINGING) {
                        this.callState=CallState.RINGING;
                        this.jingleLeg.sendSdp();
                        this.jingleLeg.addRemoteIceCandidate(candidate1);
                        //this.jingleLeg.addRemoteIceCandidate(candidate2);
                        this.jingleLeg.sendIceCandidates();
                        this.jingleLeg.sendJingleIceResults();
                    }
                }
                case HANGUP -> {
                    this.jingleLeg.endJingleA();
//                    this.jingleLeg.finishjingleA();
//                    this.jingleLeg.retarct();
                    this.callState=CallState.IDLE;
                    this.jingleLeg.callState=CallState.IDLE;
                }

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

    public CallMsgType getCallMsgType(String msg){
        if(msg.contains("CALL CREATED")){
            return CallMsgType.TRYING;
        }
        else if(msg.contains("verto.media") ){
            return CallMsgType.RINGING;
        }
        else if(msg.contains("verto.answer")){
            return CallMsgType.ANSWER;
        }
        else if(msg.contains("verto.bye")){
            return CallMsgType.HANGUP;
        }
        else return null;
    }



}
