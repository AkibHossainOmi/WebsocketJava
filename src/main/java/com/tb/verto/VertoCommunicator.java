package com.tb.verto;

import com.tb.WebSocketWrapper;
import com.tb.common.Communicator.Transport;
import com.tb.verto.msgTemplates.*;
import com.tb.webSocket.*;
import java.util.Random;
import java.util.UUID;

class VertoCommunicator extends WebSocketWrapper {
    final String login;
    final String password;
    final int pingSerialNoStart;
    final String webSocketUrl;
    Transport webSocket;
    VertoConnectParams params;
    private String callId;
    private String sessionId;
    public VertoCommunicator(VertoConnectParams params) {
        super(params.getKeepAliveParams(),params.getWebSocketSettings());
        super.setConnectMethod(()->{
            //this.webSocket =
        });
        this.params=params;
        Random random = new Random();
        this.pingSerialNoStart = 1000 + random.nextInt(1000000);
        if (params.getKeepAliveParams() !=null){
            super.setKeepAliveMethod(() -> {
                String pingMessage = Ping.createMessage(this.pingSerialNoStart);
                //webSocket.sendMessage(new WsSentData(pingMessage, true));
                //System.out.println("Sent verto ping: " + pingMessage);
            });
        }
        this.webSocket= super.connectOrReconnect();
        login();
        //ping();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendCall();
        if (getKeepAliveParam()!=null){
            startKeepAlive();
        }
        sendHangup();
        this.login = params.getLogin();//verto login
        this.password = params.getPassword();//verto pass
        this.webSocketUrl=params.getWebSocketSettings().getUri();
    }

    private void ping() {
        WsSentData data = new WsSentData(Ping.createMessage(5));
        System.out.println(data.getData());
        webSocket.sendMessage(data);
    }
    private void login() {
        sessionId = UUID.randomUUID().toString();
        WsSentData data =
                new WsSentData(Login.createMessage(params.login,
                                                    params.password,
                                                    params.webSocketSettings.getUri(),
                        sessionId,new Random().nextInt()));
        webSocket.sendMessage(data);
    }

    private void sendCall() {
        callId = UUID.randomUUID().toString();
        WsSentData data =
                new WsSentData(OutboundCall.createMessage(params.login,callId,sessionId,1));
//        System.out.println(data.getData());
        webSocket.sendMessage(data);

    }

    private void sendHangup(){
        WsSentData data =
        new WsSentData(Hangup2.createMessage(callId,564));
        System.out.println(data.getData());
        webSocket.sendMessage(data);
    }
    //@Override
    protected void startReconnectScheduler() {

    }
    public void checkConnectionState() {
    }


    @Override
    public void onOpen(WsOnOpenData data) {

    }

    @Override
    public void onClose(WsOnCloseData data) {

    }

    @Override
    public void onMessage(WsOnMsgData wsOnMsgData) {

    }

   /* @Override
    public void sendMessage(WsSentData wsSentData) {

    }*/

    @Override
    public void onError(WsOnErrorData wsOnErrorData) {

    }

    @Override
    public void onStatus(WsOnStatusData data) {

    }
}

