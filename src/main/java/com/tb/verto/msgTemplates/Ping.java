package com.tb.verto.msgTemplates;

public class Ping {
    public static String createMessage(int id) {
        return """
            {
                "id": %d,
                "jsonrpc": "2.0",
                "method": "verto.ping",
            }
            """.formatted(id); // Using String#formatted to inject the serial number
    }
}
