//package com.tb.app;
//
//import static telcobright.app.Utils.TID;
//
//import com.google.gson.Gson;
//import com.tb.app.models.JanusResponse;
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//
//import javax.net.ssl.*;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.security.cert.X509Certificate;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//
//
//public class Websocket {
//    public WebSocketClient webSocket;
//    private String userName;
//    private final String TAG = "SocketRepository";
//    private final Gson gson = new Gson();
//    private AbstractPhoneCall messageListener;
//    public Object dynamicClassInstance = new Object();
//
//
//
//    public void setCallEventListener(AbstractPhoneCall AbstractPhoneCallListener) {
//        this.messageListener = AbstractPhoneCallListener;
//    }
//
//    public void sendKeepAlive() {
//        try {
//            Field sessionIdField = dynamicClassInstance.getClass().getField("sessionId");
//            long sessionId = (long) sessionIdField.get(dynamicClassInstance);
//            System.out.println("sessionId: " + sessionId);
//            String tid = TID();
//
//            // Construct the JSON message for sending keep alive
//            String keepAliveMessage = "{ \"janus\": \"keepalive\", \"session_id\":" + sessionId + ", \"transaction\":\"" + tid + "\" }";
//
//            // Invoke the sendMessage method of the dynamic class using reflection
//            sendMessage(keepAliveMessage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void initSocket(String username) {
//        try {
//
//            Map<String,String> httpHeaders  = new HashMap<>();
//            httpHeaders.put("Sec-Websocket-Protocol","janus-protocol");
//
////            webSocket = new WebSocketClient(new URI("wss://tb.intercloud.com.bd/"),httpHeaders) {
//            webSocket = new WebSocketClient(new URI("wss://103.248.13.76/"),httpHeaders) {
//                //            webSocket = new WebSocketClient(new URI("wss://192.168.0.105/"),httpHeaders) {
////            webSocket = new WebSocketClient(new URI("wss://192.168.68.122/"),httpHeaders) {
//                @Override
//                public void onOpen(ServerHandshake handshakedata) {
//                    try {
//                        System.out.printf("Connected");
//                        // Invoke the createSession method of the dynamic class using reflection
//                        Method createSessionMethod = dynamicClassInstance.getClass().getMethod("createSession");
//                        createSessionMethod.invoke(dynamicClassInstance);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onMessage(String message) {
//                    try {
//
//                        messageListener.onNewMessage(gson.fromJson(message, JanusResponse.class));
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onClose(int code, String reason, boolean remote) {
//                    System.out.printf(reason);
//                    stopKeepAliveTimer();
//                }
//
//                @Override
//                public void onError(Exception ex) {
//                    System.out.printf(ex.toString());
//
//
//                }
//            };
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        if (webSocket != null) {
//            webSocket.connect();
//        }
//    }
//    private void disableSSLVerification() throws Exception {
//        TrustManager[] trustAllCerts = new TrustManager[]{
//                new X509TrustManager() {
//                    public X509Certificate[] getAcceptedIssuers() {
//                        return null;
//                    }
//
//                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                    }
//
//                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                    }
//                }
//        };
//
//        // Install the all-trusting trust manager
//        SSLContext sc = SSLContext.getInstance("TLS");
//        sc.init(null, trustAllCerts, new java.security.SecureRandom());
//        SSLSocketFactory noSSLFactory = sc.getSocketFactory();
//
//        // Set default SSL socket factory
//        HttpsURLConnection.setDefaultSSLSocketFactory(noSSLFactory);
//
//        // Create an all-trusting hostname verifier
//        HostnameVerifier allHostsValid = (hostname, session) -> true;
//
//        // Install the all-trusting hostname verifier
//        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//    }
//    Timer timer = new Timer("Timer");
//    public void startKeepAliveTimer() {
//        TimerTask task = new TimerTask() {
//            public void run() {
//                sendKeepAlive();
//            }
//        };
//
//        long delay = 1000L;
//        long interval = 25000L;
//        timer.schedule(task, delay, interval);
//    }
//
//    public void stopKeepAliveTimer() {
//        if (timer != null) {
//            timer.cancel();
//            timer.purge();
//        }
//    }
//    public void closeSocket() {
//        if (webSocket != null && webSocket.isOpen()) {
//            webSocket.close();
//        }
//    }
//
//    public void sendMessage(String message) {
//        try {
//            System.out.println(TAG + "sendMessageToSocket: " + message);
//            if (webSocket != null) {
//
//                webSocket.send(message);
//            }
//        } catch (Exception e) {
//            System.out.println(TAG+ " sendMessageToSocket: " + e);
//        }
//    }
//}
