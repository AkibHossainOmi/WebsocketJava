package com.tb;

import com.tb.calling.VertoCall;
import com.tb.common.Communicator.ServicePingParams;
import com.tb.common.WebSocketType;
import com.tb.verto.VertoConnectParams;
import com.tb.verto.VertoProxy;
import com.tb.webSocket.WebSocketSettings;

import java.util.Scanner;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        ServicePingParams servicePingParams = new ServicePingParams(1,1);
        VertoConnectParams params= new VertoConnectParams("1001",
                "1234",
                new WebSocketSettings(WebSocketType.Ws,"ws://192.168.0.31:8081",1000),
                new ServicePingParams(1,1));

        VertoProxy verto= new VertoProxy(params);
        VertoCall newCall= new VertoCall(UUID.randomUUID().toString(),"1001","9999");

        // Wait for a keystroke before exiting
        System.out.println("Press Enter to exit...");
        new Scanner(System.in).nextLine();
    }
}
