package com.tb.calling.verto.msgTemplates;

public class Echo {
    public static String createMessage(int id) {
        return """
                {
                  "jsonrpc": "2.0",
                  "method": "echo",
                  "params": {
                    "keepalive": true
                  }
                }
            """.formatted(id); // Using String#formatted to inject the serial number
    }
}
