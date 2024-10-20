package com.tb.calling.jingle;
import com.tb.calling.*;
import com.tb.calling.jingle.msgTemplates.*;
import com.tb.calling.verto.VertoConnector;
import com.tb.common.eventDriven.RequestAndResponse.Conversations.JingleICE;
import com.tb.common.eventDriven.RequestAndResponse.Conversations.JingleSDP;
import com.tb.common.eventDriven.RequestAndResponse.Conversations.JingleMsgType;
import com.tb.common.eventDriven.RequestAndResponse.Enums.TransportPacket;
import com.tb.common.StringUtil;
import com.tb.common.UUIDGen;
import com.tb.common.eventDriven.RequestAndResponse.Payload;
import com.tb.common.eventDriven.RequestAndResponse.MultiThreadedRequestHandler;
import com.tb.common.uniqueIdGenerator.ShortIdGenerator;


import java.io.IOException;

public class JingleCallLeg extends AbstractCallLeg {
    VertoCallLeg vertoCall;
    MultiThreadedRequestHandler multiThreadedRequestHandler;
    public JingleConnector getJingleConnector() {
        return jingleConnector;
    }

    JingleConnector jingleConnector;

    public VertoConnector getVertoConnector() {
        return vertoConnector;
    }

    public void setVertoConnector(VertoConnector vertoConnector) {
        this.vertoConnector = vertoConnector;
    }

    VertoConnector vertoConnector;
    public JingleCallLeg(JingleConnector connector) {
        super(connector);
        connector.addPublicListener(this);
        this.jingleConnector=connector;
        this.multiThreadedRequestHandler =
                new MultiThreadedRequestHandler(this.getJingleConnector().restTransport);
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

            String aPartyWithDevice= StringUtil.Parser
                    .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "from=",",");
            String[] tempArr=aPartyWithDevice.split("/");
            this.setaParty(tempArr[0]);
            this.setaPartyDeviceId(tempArr[1]);

            this.setbParty(StringUtil.Parser
                    .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "to=",","));

            this.setbPartyDeviceId(this.jingleConnector.xmppSettings.deviceId);
            // Extract the ID from the message
            this.setUniqueId(msg.substring(startIndex, endIndex));
            this.accept();
            this.proceed();
        }
        if(msg.contains("session-initiate")){
            JingleSDP jingleSDP= new JingleSDP(msg, JingleMsgType.SDP);
            assert(!this.getaParty().isEmpty() && !this.getaPartyDeviceId().isEmpty());
            assert(!this.getbParty().isEmpty() && !this.getbPartyDeviceId().isEmpty());
            jingleSDP.getMetadata().put("bParty",this.getbParty()+"/" + this.getbPartyDeviceId());
            jingleSDP.getMetadata().put("aParty",this.getaParty()+"/" + this.getaPartyDeviceId());
            this.multiThreadedRequestHandler.dispatch(jingleSDP);
        }

        if(msg.contains("transport-info")){//on ice
            JingleICE jingleICE = new JingleICE(msg, JingleMsgType.ICE);
            assert(!this.getaParty().isEmpty() && !this.getaPartyDeviceId().isEmpty());
            assert(!this.getbParty().isEmpty() && !this.getbPartyDeviceId().isEmpty());
            jingleICE.getMetadata().put("bParty",this.getbParty()+"/" + this.getbPartyDeviceId());
            jingleICE.getMetadata().put("aParty",this.getaParty()+"/" + this.getaPartyDeviceId());
            this.multiThreadedRequestHandler.dispatch(jingleICE);
            if(this.callState==CallState.SESSION_START){
                this.callState=CallState.CALLER_SDP_RECEIVED;
            }
            if(this.callState==CallState.CALLER_SDP_RECEIVED){
                int portIndex = msg.indexOf("port=&apos;") + "port=&apos;".length();
                String subStr= msg.substring(portIndex);
                int port= Integer.parseInt(subStr.split("&")[0]);
                if (port<=0)
                    throw new RuntimeException("Media Port must be >0 ");
                int ipIndex = msg.indexOf("ip=&apos;") + "ip=&apos;".length();
                subStr= msg.substring(ipIndex);
                String ip= subStr.split("&")[0];

                ICECandidate candidate= new ICECandidate(ShortIdGenerator.getNext(), ip,
                        port, CandidateType.HOST, TransportProtocol.UDP);
                this.vertoCall= new VertoCallLeg(this.vertoConnector);
                this.vertoCall.setUniqueId(UUIDGen.getNextAsStr());
                this.vertoCall.setaParty("09646888888");
                this.vertoCall.setbParty("01754105098");
                this.vertoCall.addRemoteIceCandidate(candidate);
                this.vertoCall.getConnector().addPublicListener(this.vertoCall);
                this.vertoCall.setJingleCall(this);
                this.vertoCall.startSession();
                this.callState=CallState.WAITING_RINGING;
            }
        }

    }

    public void sendIceCandidates() {
        for (ICECandidate candidate : this.remoteIceCandidates.values()) {
            String ip = candidate.getIpAddress();
            int port = candidate.getPort();
            String ice = Ice.createMessage(getaParty()+"/"+getaPartyDeviceId(),
                    getbParty()+"/"+getbPartyDeviceId(),this.getUniqueId(),
                    ip, port);
            Payload payload= new Payload(UUIDGen.getNextAsStr(),ice, TransportPacket.Payload);
            payload.getMetadata().put("useRest", true);
            this.getConnector().sendMsgToConnector(payload);
        }
    }

    public void sendSdp() {
        String sdp = SDP.createMessage(getaParty()+"/"+getaPartyDeviceId(),
                getbParty()+"/"+getbPartyDeviceId(),this.getUniqueId());
        Payload s= new Payload(UUIDGen.getNextAsStr(),sdp, TransportPacket.Payload);
        s.getMetadata().put("useRest", true);
        this.getConnector().sendMsgToConnector(s);
    }

    public void proceed() {
        String proceed = Proceed.createMessage(getaParty()+"/"+getaPartyDeviceId(),
                getbParty()+"/"+getbPartyDeviceId(), this.getUniqueId());
        Payload p= new Payload(UUIDGen.getNextAsStr(),proceed, TransportPacket.Payload);
        p.getMetadata().put("useRest", true);
        this.getConnector().sendMsgToConnector(p);
    }

    public void accept() {
        // Call Accept class and pass extractedId
        String accept= Accept.createMessage( getbParty()+"/"+getbPartyDeviceId(), getbParty(), this.getUniqueId());
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
