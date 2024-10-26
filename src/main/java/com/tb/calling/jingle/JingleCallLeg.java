package com.tb.calling.jingle;
import com.tb.calling.base.AbstractCallLeg;
import com.tb.calling.base.ICECandidate;
import com.tb.calling.base.statemachine.Transition;
import com.tb.calling.jingle.msgTemplates.*;
import com.tb.calling.verto.VertoCallLeg;
import com.tb.calling.verto.VertoConnector;
import com.tb.calling.jingle.ConversationsRequests.JingleICE;
import com.tb.calling.jingle.ConversationsRequests.JingleMsgType;
import com.tb.common.CallEvent;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CallState;
import com.tb.common.eventDriven.RequestAndResponse.Enums.CandidateType;
import com.tb.common.eventDriven.RequestAndResponse.Enums.TransportPacket;
import com.tb.common.StringUtil;
import com.tb.common.UUIDGen;
import com.tb.common.eventDriven.RequestAndResponse.Enums.TransportProtocol;
import com.tb.common.eventDriven.RequestAndResponse.Payload;
import com.tb.common.eventDriven.RequestAndResponse.MultiThreadedRequestHandler;
import com.tb.common.uniqueIdGenerator.ShortIdGenerator;
import com.tb.calling.jingle.ConversationsRequests.ProposeResponse;

import java.util.ArrayList;
import java.util.List;

public class JingleCallLeg extends AbstractCallLeg {
    SDPMessageFactory sdpMessageFactory;
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

    final List<JingleICE> jingleIceCandidates = new ArrayList<>();

    VertoConnector vertoConnector;
    public JingleCallLeg(JingleConnector connector) {
        super(connector);
        connector.addPublicListener(this);
        this.jingleConnector=connector;
        this.multiThreadedRequestHandler =
                new MultiThreadedRequestHandler(this.getJingleConnector().restTransport);

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
        String msg = data.getData();

        if (msg.contains("session-initiate")) {

        }

        if (msg.contains("transport-info")) {//on ice

            String id = StringUtil.Parser
                    .getFirstOccuranceOfParamValueByIndexAndTerminatingStr(msg, "priority=&apos;", "&apos;");

            JingleICE jingleICE = new JingleICE(msg, JingleMsgType.ICE);
            assert (!this.getaParty().isEmpty() && !this.getaPartyDeviceId().isEmpty());
            assert (!this.getbParty().isEmpty() && !this.getbPartyDeviceId().isEmpty());
            jingleICE.getMetadata().put("bParty", this.getbParty() + "/" + this.getbPartyDeviceId());
            jingleICE.getMetadata().put("aParty", this.getaParty() + "/" + this.getaPartyDeviceId());
            this.jingleIceCandidates.add(jingleICE);
            if (this.callState == CallState.SESSION_START) {
                this.callState = CallState.CALLER_SDP_RECEIVED;
            }
            if (this.callState == CallState.CALLER_SDP_RECEIVED) {
                int portIndex = msg.indexOf("port=&apos;") + "port=&apos;".length();
                String subStr = msg.substring(portIndex);
                int port = Integer.parseInt(subStr.split("&")[0]);
                if (port <= 0)
                    throw new RuntimeException("Media Port must be >0 ");
                int ipIndex = msg.indexOf("ip=&apos;") + "ip=&apos;".length();
                subStr = msg.substring(ipIndex);
                String ip = subStr.split("&")[0];

                ICECandidate candidate = new ICECandidate(ShortIdGenerator.getNext(), ip,
                        port, CandidateType.HOST, TransportProtocol.UDP);
                this.vertoCall = new VertoCallLeg(this.vertoConnector);
                this.vertoCall.setUniqueId(UUIDGen.getNextAsStr());
                this.vertoCall.setaParty("09646888888");
                this.vertoCall.setbParty("01754105098");
                this.vertoCall.addRemoteIceCandidate(candidate);
                this.vertoCall.getConnector().addPublicListener(this.vertoCall);
                this.vertoCall.setJingleLeg(this);
                this.vertoCall.startSession();
                this.callState = CallState.WAITING_RINGING;
            }
        }
    }

    @Override
    public void onTransportError(Payload payload) {

    }

    @Override
    public void onTransportStatus(Payload payload) {

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
            String ice1= this.sdpMessageFactory.createICE1Message();
            Payload payload= new Payload(UUIDGen.getNextAsStr(),ice1, TransportPacket.Payload);
            payload.getMetadata().put("useRest", true);
            this.getConnector().sendMsgToConnector(payload);
        }
    }

    public void sendSdp() {
        /*String sdp = SDP.createMessage(getaParty()+"/"+getaPartyDeviceId(),
                getbParty()+"/"+getbPartyDeviceId(),this.getUniqueId());*/
        String sdp = this.sdpMessageFactory.createSDPMessage();
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

    public void ringing() {
        // Call Accept class and pass extractedId
        String ringing= Ringing.createMessage( getbParty()+"/"+getbPartyDeviceId(), getaParty()+"/"+getaPartyDeviceId(), this.getUniqueId());
        Payload p= new Payload(UUIDGen.getNextAsStr(),ringing, TransportPacket.Payload);
        p.getMetadata().put("useRest", true);
        this.getConnector().sendMsgToConnector(p);
    }

    @Override
    public void onStateChange(Transition transition, CallEvent event) {
        switch (event.getEventType()) {
            case SESSION_START -> {
            }
            case TRYING -> {
            }
            case RINGING -> {
            }
            case ANSWER -> {
            }
            case HANGUP -> {
            }
            case SDP -> {
            }
            case ICE_CANDIDATE -> {
            }
            case ICE_ACK -> {
            }
            case UNKNOWN -> {
            }
        }
    }
}
