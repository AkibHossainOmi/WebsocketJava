package com.tb.common.eventDriven.RequestAndResponse.Enums;

public enum TransportProtocol {
    UDP("udp"),
    TCP("tcp"),
    TLS("tls");

    private final String protocol;

    TransportProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    @Override
    public String toString() {
        return protocol;
    }
}