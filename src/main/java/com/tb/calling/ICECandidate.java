package com.tb.calling;

import com.tb.common.eventDriven.RequestAndResponse.Enums.CandidateType;
import com.tb.common.eventDriven.RequestAndResponse.Enums.TransportProtocol;

public class ICECandidate {
    public String getId() {
        return id;
    }

    private String id;
    private String ipAddress;
    private int port;
    private CandidateType candidateType;
    private TransportProtocol transportProtocol;

    // Constructor
    public ICECandidate(String id, String ipAddress, int port,
                        CandidateType candidateType, TransportProtocol transportProtocol) {
        this.id=id;
        this.ipAddress = ipAddress;
        this.port = port;
        this.candidateType = candidateType;
        this.transportProtocol = transportProtocol;
    }

    // Getters and Setters
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public CandidateType getCandidateType() {
        return candidateType;
    }

    public void setCandidateType(CandidateType candidateType) {
        this.candidateType = candidateType;
    }

    public TransportProtocol getTransportProtocol() {
        return transportProtocol;
    }

    public void setTransportProtocol(TransportProtocol transportProtocol) {
        this.transportProtocol = transportProtocol;
    }

    // Overriding toString() to print the ICE info easily
    @Override
    public String toString() {
        return "ICEInfo{" +
                "ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                ", candidateType=" + candidateType +
                ", transportProtocol=" + transportProtocol +
                '}';
    }
}

