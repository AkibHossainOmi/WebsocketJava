package com.tb.calling.verto.msgTemplates;

public class PingResult {
    public static String createMessage(int id) {
        return """
            {"jsonrpc":"2.0","id":%d,"result":{"method":"verto.ping"}}
            """.formatted(id); // Using String#formatted to inject the serial number
    }
}
