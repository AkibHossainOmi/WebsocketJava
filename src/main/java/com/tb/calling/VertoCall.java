package com.tb.calling;

import com.tb.common.eventDriven.Connector;
import com.tb.common.eventDriven.Payload;
import com.tb.verto.msgTemplates.ModifyCall;
import com.tb.verto.msgTemplates.StartCall;

import java.io.IOException;
import java.util.UUID;

public class VertoCall extends AbstractPhoneCall {
    @Override
    public void startCall() {
        System.out.println("sending Invite...");
        this.setUniqueId(UUID.randomUUID().toString());
        String data =StartCall.createMessage("1001",this.getUniqueId(),connector.getSessionId(),100);
        connector.sendMessage(new Payload(data));
    }
    public void modifyCall() {
        System.out.println("Sending Modify...");
        String data = ModifyCall.createMessage("1001",this.getUniqueId(),connector.getSessionId(),120);
        connector.sendMessage(new Payload(data));
    }
    @Override
    public void onStart(Object message) {

    }

    @Override
    public void onNewMessage(Object message) throws IOException {

    }

    @Override
    public void startSession(Object message) {

    }

    @Override
    public void startSession() {

    }

    @Override
    public void updateSession() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void onRing() {

    }

    @Override
    public void onAnswer() {

    }

    public VertoCall(Connector connector, String uniqueId, String aparty, String bparty) {
        super(connector, uniqueId, aparty, bparty);
    }
}
