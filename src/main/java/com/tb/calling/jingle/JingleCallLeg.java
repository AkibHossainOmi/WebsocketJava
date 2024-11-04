package com.tb.calling.jingle;
import com.tb.calling.base.AbstractCallLeg;
import com.tb.calling.base.AbstractCallStack;
import com.tb.calling.base.statemachine.Transition;
import com.tb.calling.jingle.message.templates.Accept;
import com.tb.calling.jingle.message.templates.Proceed;
import com.tb.calling.jingle.message.templates.Ringing;
import com.tb.calling.verto.VertoConnector;
import com.tb.calling.jingle.Conversations.JingleICE;
import com.tb.calling.jingle.Conversations.JingleMsgType;
import com.tb.common.SignalingEvent;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;
import com.tb.common.eventDriven.RequestAndResponse.Enums.TransportPacket;
import com.tb.common.StringUtil;
import com.tb.common.UUIDGen;
import com.tb.common.eventDriven.RequestAndResponse.Payload;
import com.tb.calling.jingle.Conversations.ProposeResponse;

import java.util.ArrayList;
import java.util.List;

public class JingleCallLeg extends AbstractCallLeg {
    SDPMessageFactory sdpMessageFactory;
    AbstractCallStack stack;
    final List<JingleICE> jingleIceCandidates = new ArrayList<>();
    VertoConnector vertoConnector;
    public JingleCallLeg(JingleStack stack) {
        super(stack);
    }
    public void sendJingleIceResults(){
        for (JingleICE jingleIceCandidate : jingleIceCandidates) {
            this.multiThreadedRequestHandler.sendResponse(jingleIceCandidate);
        }
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

    }

    private void proposeResponse(String msg) {
        String id = StringUtil.Parser
                .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "id=",
                        "readToEndOfStr");
        ProposeResponse proposeResponse =
                new ProposeResponse(msg, JingleMsgType.PROPOSE_RESPONSE);
        assert (!this.getaParty().isEmpty() && !this.getaPartyDeviceId().isEmpty());
        assert (!this.getbParty().isEmpty() && !this.getbPartyDeviceId().isEmpty());
        proposeResponse.getMetadata().put("bParty", this.getbParty() + "/" + this.getbPartyDeviceId());
        proposeResponse.getMetadata().put("aParty", this.getaParty() + "/" + this.getaPartyDeviceId());
        this.multiThreadedRequestHandler.sendResponse(proposeResponse);
    }

    public void sendIceCandidates() {
        for (ICECandidate candidate : this.remoteIceCandidates.values()) {
            String ip = candidate.getIpAddress();
            int port = candidate.getPort();
            /*String ice = ICE1.createMessage(getaParty()+"/"+getaPartyDeviceId(),
                    getbParty()+"/"+getbPartyDeviceId(),this.getUniqueId(),
                    ip, port,this.getPriorityId());*/
            String ice1= this.sdpMessageFactory.createICEMessage();
            Payload payload= new Payload(UUIDGen.getNextAsStr(),ice1, TransportPacket.Payload);
            payload.getMetadata().put("useRest", true);
            this.getConnector().sendMsgToConnector(payload);
        }
    }
    public void sendSdp() {

        this.sdpMessageFactory = new SDPMessageFactory(this.getbParty() + "/" + this.getbPartyDeviceId(),
                this.getaParty() + "/" + this.getaPartyDeviceId(),
                this.getSessionId(),
                vertoCall.getVertoSdpParamA().getSsrc(),
                vertoCall.getVertoSdpParamA().getMsid(),
                vertoCall.getVertoSdpParamA().getUfrag(),
                vertoCall.getVertoSdpParamA().getPwd(),
                vertoCall.getVertoSdpParamA().getFingerprint(),getPriorityId(),
                vertoCall.getVertoSdpParamA().getIp(),vertoCall.getVertoSdpParamA().getPort());
        /*String sdp = SDP.createMessage(getaParty()+"/"+getaPartyDeviceId(),
                getbParty()+"/"+getbPartyDeviceId(),this.getUniqueId());*/
        String sdp = this.sdpMessageFactory.createSDPMessage();
        //String sdp = this.sdpMessageFactory.createSDPWithICEMessage();
//        String sdp = "Hello SDP";
        Payload s= new Payload(UUIDGen.getNextAsStr(),sdp, TransportPacket.Payload);
        s.getMetadata().put("useRest", true);
        this.getConnector().sendMsgToConnector(s);
    }

    public void proceed() {
        String proceed = Proceed.createMessage(getaParty()+"/"+getaPartyDeviceId(),
                getbParty()+"/"+getbPartyDeviceId(), this.getSessionId());
        Payload p= new Payload(UUIDGen.getNextAsStr(),proceed, TransportPacket.Payload);
        p.getMetadata().put("useRest", true);
        this.getConnector().sendMsgToConnector(p);
    }

    public void accept() {
        // Call Accept class and pass extractedId
        String accept= Accept.createMessage( getbParty()+"/"+getbPartyDeviceId(), getbParty(), this.getSessionId());
        Payload p= new Payload(UUIDGen.getNextAsStr(),accept, TransportPacket.Payload);
        p.getMetadata().put("useRest", true);
        this.getConnector().sendMsgToConnector(p);
    }

    public void ringing() {
        // Call Accept class and pass extractedId
        String ringing= Ringing.createMessage( getbParty()+"/"+getbPartyDeviceId(), getaParty()+"/"+getaPartyDeviceId(), this.getSessionId());
        Payload p= new Payload(UUIDGen.getNextAsStr(),ringing, TransportPacket.Payload);
        p.getMetadata().put("useRest", true);
        this.getConnector().sendMsgToConnector(p);
    }

//    public void proposeResponse() {
//        // Call Accept class and pass extractedId
//        String proposeResponse= ProposeResponse.createMessage( getbParty()+"/"+getbPartyDeviceId(), getbParty(), this.getUniqueId());
//        Payload p= new Payload(UUIDGen.getNextAsStr(),proposeResponse, TransportPacket.Payload);
//        p.getMetadata().put("useRest", true);
//        this.getConnector().sendMsgToConnector(p);
//        this.multiThreadedRequestHandler.sendResponse(jingleSDP);
//    }

    @Override
    public void onTransportError(Payload payload) {

    }

    @Override
    public void onTransportStatus(Payload payload) {

    }


    @Override
    public void onStateChange(Transition transition, SignalingEvent msg) {

    }

    @Override
    public void onInbandStateMessage(CallState state, SignalingEvent msg) {

    }
}
