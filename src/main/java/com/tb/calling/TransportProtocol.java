package com.tb.calling;

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