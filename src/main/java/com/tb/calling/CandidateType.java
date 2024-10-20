package com.tb.calling;

public enum CandidateType {
    HOST("host"),
    SRFLX("srflx"),  // Server Reflexive
    RELAY("relay"),
    PRFLX("prflx");  // Peer Reflexive

    private final String type;

    CandidateType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }
}