package com.tb.verto;
import com.tb.calling.CallState;
import com.tb.calling.CallStateTracker;
import com.tb.calling.VertoCall;
import com.tb.common.Communicator.ConnectionStatus;
import com.tb.common.adapters.ProxyClient;
import com.tb.common.adapters.ProxyServer;
import com.tb.webSocket.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Assuming the necessary imports for the WebSocket data types
public class VertoProxy implements
        ProxyServer<WsOnOpenData, WsOnCloseData, WsOnMsgData, WsSentData, WsOnErrorData, WsOnStatusData>,
        ProxyClient<WsOnOpenData, WsOnCloseData, WsOnMsgData, WsSentData, WsOnErrorData, WsOnStatusData> {
    private VertoCommunicator server; // Server-side communicator
    private final CallStateTracker callStateTracker = new CallStateTracker(); // State tracker
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final int MAX_CALL_DURATION_MINUTES = 120; // Predefined call duration limit
    public VertoProxy(VertoConnectParams params) {
        this.server= new VertoCommunicator(params);
        server.connectOrReconnect();
        // Start the scheduler to monitor call duration
        scheduler.scheduleAtFixedRate(this::checkCallDurations, 0, 1, TimeUnit.MINUTES);
    }
    public void addCall(VertoCall newCall){
        callStateTracker.updateCallState(newCall.getUniqueId(), CallState.SESSION_START);
    }
    // ProxyServer Methods mapped to VertoCommunicator (Server)
    @Override
    public void onServerOpen(WsOnOpenData data) {
        server.onOpen(data);
    }

    @Override
    public void onServerClose(WsOnCloseData data) {
        server.onClose(data);
    }

    @Override
    public void onServerMessage(WsOnMsgData data) {
        server.onMessage(data);
    }

    @Override
    public void sendMessageServer(WsSentData data) {
        server.sendMessage(data);
    }

    @Override
    public void onServerError(WsOnErrorData data) {
        server.onError(data);
    }

    @Override
    public void onServerStatus(WsOnStatusData data) {
        server.onStatus(data);
    }

    @Override
    public ConnectionStatus getConnectionStatus() {
        return server.getConnectionStatus();
    }

    // ProxyClient Methods mapped to VertoCall (Client)
    @Override
    public void onClientOpen(WsOnOpenData data) {
        //client.startCall();
    }

    @Override
    public void onClientClose(WsOnCloseData data) {
        //client.disconnect();
    }

    @Override
    public void onClientMessage(WsOnMsgData data) {
     /*   try {
            //client.onNewMessage(data);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void SendMessageClient(WsSentData data) {
        //client.startSession(data);
    }

    @Override
    public void onClientError(WsOnErrorData data) {
        // Handle client error if needed
    }

    @Override
    public void onClientStatus(WsOnStatusData data) {
        // Handle client status if needed
    }

    // Ensure calls don't exceed the predefined duration
    private void checkCallDurations() {
        callStateTracker.getCallDictionary().forEach((callId, callInfo) -> {
            if (callInfo.timestamp.isBefore(LocalDateTime.now().minusMinutes(MAX_CALL_DURATION_MINUTES))) {
                // If the call duration exceeds the limit, send a release message
                System.out.println("Call " + callId + " exceeded duration limit. Disconnecting...");
                // Mock release message
                sendToClient(callId, Map.of("callId", callId, "message", "Call released due to timeout"));
                callStateTracker.removeCall(callId);
            }
        });
    }

    private void sendToClient(String callId, Map<String, String> message) {
        // Mock implementation for sending a message to the Verto client
        System.out.println("Message sent to Verto client for callId " + callId + ": " + message);
    }
}
