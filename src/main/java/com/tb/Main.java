package com.tb;

import com.tb.calling.VertoCall;
import com.tb.common.WebSocketType;
import com.tb.common.eventDriven.ServicePingParams;
import com.tb.calling.verto.VertoConnectParams;
import com.tb.calling.verto.VertoConnector;
import com.tb.transport.websocket.WebSocketSettings;

import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        VertoConnectParams params = new VertoConnectParams("1001",
                "1234",
                new WebSocketSettings(WebSocketType.Ws, "ws://192.168.0.31:8081", 1000),
                new ServicePingParams());
        VertoConnector vc = new VertoConnector(params);
        vc.connect();
        delay(1000);
        vc.login();
        delay(3000);
        vc.ping();

        VertoCall newCall= new VertoCall(vc, UUID.randomUUID().toString(),"1001","9999");
        newCall.startCall();
        delay(10000);
        newCall.modifyCall();

        // Wait for a keystroke before exiting
        System.out.println("Press Enter to exit...");
        new Scanner(System.in).nextLine();
    }

    private static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
