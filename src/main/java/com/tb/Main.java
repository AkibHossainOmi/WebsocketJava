package com.tb;

import com.tb.calling.jingle.JingleCallLeg;
import com.tb.calling.jingle.JingleConnector;
import com.tb.common.WebSocketType;
import com.tb.common.eventDriven.ServicePingParams;
import com.tb.calling.verto.VertoConnectParams;
import com.tb.calling.verto.VertoConnector;
import com.tb.transport.rest.RestSettings;
import com.tb.transport.websocket.WebSocketSettings;
import com.tb.transport.xmpp.XmppSettings;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;


import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws XmppStringprepException {
        VertoConnectParams params = new VertoConnectParams("09646888888",
                "09646888888##asdf",
                new WebSocketSettings(WebSocketType.Ws, "ws://iptsp.cosmocom.net:8081", 1000),
                new ServicePingParams());
        VertoConnector vc = new VertoConnector(params);
        vc.connectOrInit();
        delay(1000);
        vc.login();

        RestSettings restSettings= new RestSettings("http://192.168.0.31:5280/rest");
        XmppSettings xmppSettings= new XmppSettings("192.168.0.31",5222,"test6",
                "test123","localhost","Conversations.restB",
                ConnectionConfiguration.SecurityMode.disabled,1);
        JingleConnector jingleConnector= new JingleConnector(xmppSettings,restSettings);
        jingleConnector.connectOrInit();
        JingleCallLeg jingleCall= new JingleCallLeg(jingleConnector);
        /*XmppRun xmppRun = new XmppRun();
        xmppRun.XmppInstance();*/
        // Wait for a keystroke before exiting
        jingleCall.setVertoConnector(vc);
        System.out.println("Press Enter to continue...");
        new Scanner(System.in).nextLine();


        //delay(3000);
        //vc.ping();



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
