//package com.tb.app.websocketBaseCaller.callers;
//
//import static com.tb.app.utils.KeepAliveMessage.websocket;
//import static telcobright.app.Utils.TID;
//import static telcobright.app.Utils.getPublicIP;
//
//
//import com.google.gson.Gson;
//import com.tb.app.AbstractPhoneCall;
//import com.tb.app.Websocket;
//import com.tb.app.models.JanusMessage;
//import com.tb.app.models.JanusResponse;
//import org.java_websocket.client.WebSocketClient;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//import java.util.Timer;
//import java.util.TimerTask;
//import telcobright.app.utils.SDPParser;
//
//public class JanusCall extends AbstractPhoneCall {
//    public WebSocketClient webSocket;
//    private final Gson gson = new Gson();
//    public static long sessionId = 0;
//    private long handleId;
//    private String sipUserName;
//    private String receiver;
//    private String calledNumber;
//    private String callingNumber;
//    private String secretKey;
//    private String sdp = "[offer]\nv=0\no=- 7073247130280359289 3 IN IP4 1.1.1.1\ns=-\nt=0 0\nm=audio 20120 RTP/AVP 111 63 9 0 8 13 110 126\nc=IN IP4 217.61.26.125\na=sendrecv\na=mid:0\na=extmap:1 urn:ietf:params:rtp-hdrext:ssrc-audio-level\na=extmap:2 http://www.webrtc.org/experiments/rtp-hdrext/abs-send-time\na=extmap:3 http://www.ietf.org/id/draft-holmer-rmcat-transport-wide-cc-extensions-01\na=extmap:4 urn:ietf:params:rtp-hdrext:sdes:mid\na=msid:215e6059-515a-45ca-ada3-84e98f467771 419b53e3-3506-47a9-9ff1-340479aef41c\na=rtpmap:111 opus/48000/2\na=rtcp-fb:111 transport-cc\na=fmtp:111 minptime=10;useinbandfec=1\na=rtpmap:63 red/48000/2\na=fmtp:63 111/111\na=rtpmap:9 G722/8000\na=rtpmap:0 PCMU/8000\na=rtpmap:8 PCMA/8000\na=rtpmap:13 CN/8000\na=rtpmap:110 telephone-event/48000\na=rtpmap:126 telephone-event/8000\nm=video 20122 RTP/AVP 96 102 104 106 108 127 39 45 98 100 112 35 37 41 43 47 114\nc=IN IP4 217.61.26.125\na=recvonly\na=mid:1\na=extmap:14 urn:ietf:params:rtp-hdrext:toffset\na=extmap:2 http://www.webrtc.org/experiments/rtp-hdrext/abs-send-time\na=extmap:13 urn:3gpp:video-orientation\na=extmap:3 http://www.ietf.org/id/draft-holmer-rmcat-transport-wide-cc-extensions-01\na=extmap:5 http://www.webrtc.org/experiments/rtp-hdrext/playout-delay\na=extmap:6 http://www.webrtc.org/experiments/rtp-hdrext/video-content-type\na=extmap:7 http://www.webrtc.org/experiments/rtp-hdrext/video-timing\na=extmap:8 http://www.webrtc.org/experiments/rtp-hdrext/color-space\na=extmap:4 urn:ietf:params:rtp-hdrext:sdes:mid\na=extmap:10 urn:ietf:params:rtp-hdrext:sdes:rtp-stream-id\na=extmap:11 urn:ietf:params:rtp-hdrext:sdes:repaired-rtp-stream-id\na=msid:215e6059-515a-45ca-ada3-84e98f467771 785a4561-5702-4cbe-b116-bc55545e1b4c\na=rtpmap:96 VP8/90000\na=rtcp-fb:96 goog-remb\na=rtcp-fb:96 transport-cc\na=rtcp-fb:96 ccm fir\na=rtcp-fb:96 nack\na=rtcp-fb:96 nack pli\na=rtpmap:102 H264/90000\na=rtcp-fb:102 goog-remb\na=rtcp-fb:102 transport-cc\na=rtcp-fb:102 ccm fir\na=rtcp-fb:102 nack\na=rtcp-fb:102 nack pli\na=fmtp:102 level-asymmetry-allowed=1;packetization-mode=1;profile-level-id=42001f\na=rtpmap:104 H264/90000\na=rtcp-fb:104 goog-remb\na=rtcp-fb:104 transport-cc\na=rtcp-fb:104 ccm fir\na=rtcp-fb:104 nack\na=rtcp-fb:104 nack pli\na=fmtp:104 level-asymmetry-allowed=1;packetization-mode=0;profile-level-id=42001f\na=rtpmap:106 H264/90000\na=rtcp-fb:106 goog-remb\na=rtcp-fb:106 transport-cc\na=rtcp-fb:106 ccm fir\na=rtcp-fb:106 nack\na=rtcp-fb:106 nack pli\na=fmtp:106 level-asymmetry-allowed=1;packetization-mode=1;profile-level-id=42e01f\na=rtpmap:108 H264/90000\na=rtcp-fb:108 goog-remb\na=rtcp-fb:108 transport-cc\na=rtcp-fb:108 ccm fir\na=rtcp-fb:108 nack\na=rtcp-fb:108 nack pli\na=fmtp:108 level-asymmetry-allowed=1;packetization-mode=0;profile-level-id=42e01f\na=rtpmap:127 H264/90000\na=rtcp-fb:127 goog-remb\na=rtcp-fb:127 transport-cc\na=rtcp-fb:127 ccm fir\na=rtcp-fb:127 nack\na=rtcp-fb:127 nack pli\na=fmtp:127 level-asymmetry-allowed=1;packetization-mode=1;profile-level-id=4d001f\na=rtpmap:39 H264/90000\na=rtcp-fb:39 goog-remb\na=rtcp-fb:39 transport-cc\na=rtcp-fb:39 ccm fir\na=rtcp-fb:39 nack\na=rtcp-fb:39 nack pli\na=fmtp:39 level-asymmetry-allowed=1;packetization-mode=0;profile-level-id=4d001f\na=rtpmap:45 AV1/90000\na=rtcp-fb:45 goog-remb\na=rtcp-fb:45 transport-cc\na=rtcp-fb:45 ccm fir\na=rtcp-fb:45 nack\na=rtcp-fb:45 nack pli\na=fmtp:45 level-idx=5;profile=0;tier=0\na=rtpmap:98 VP9/90000\na=rtcp-fb:98 goog-remb\na=rtcp-fb:98 transport-cc\na=rtcp-fb:98 ccm fir\na=rtcp-fb:98 nack\na=rtcp-fb:98 nack pli\na=fmtp:98 profile-id=0\na=rtpmap:100 VP9/90000\na=rtcp-fb:100 goog-remb\na=rtcp-fb:100 transport-cc\na=rtcp-fb:100 ccm fir\na=rtcp-fb:100 nack\na=rtcp-fb:100 nack pli\na=fmtp:100 profile-id=2\na=rtpmap:112 H264/90000\na=rtcp-fb:112 goog-remb\na=rtcp-fb:112 transport-cc\na=rtcp-fb:112 ccm fir\na=rtcp-fb:112 nack\na=rtcp-fb:112 nack pli\na=fmtp:112 level-asymmetry-allowed=1;packetization-mode=1;profile-level-id=64001f\na=rtpmap:35 VP9/90000\na=rtcp-fb:35 goog-remb\na=rtcp-fb:35 transport-cc\na=rtcp-fb:35 ccm fir\na=rtcp-fb:35 nack\na=rtcp-fb:35 nack pli\na=fmtp:35 profile-id=1\na=rtpmap:37 VP9/90000\na=rtcp-fb:37 goog-remb\na=rtcp-fb:37 transport-cc\na=rtcp-fb:37 ccm fir\na=rtcp-fb:37 nack\na=rtcp-fb:37 nack pli\na=fmtp:37 profile-id=3\na=rtpmap:41 H264/90000\na=rtcp-fb:41 goog-remb\na=rtcp-fb:41 transport-cc\na=rtcp-fb:41 ccm fir\na=rtcp-fb:41 nack\na=rtcp-fb:41 nack pli\na=fmtp:41 level-asymmetry-allowed=1;packetization-mode=1;profile-level-id=f4001f\na=rtpmap:43 H264/90000\na=rtcp-fb:43 goog-remb\na=rtcp-fb:43 transport-cc\na=rtcp-fb:43 ccm fir\na=rtcp-fb:43 nack\na=rtcp-fb:43 nack pli\na=fmtp:43 level-asymmetry-allowed=1;packetization-mode=0;profile-level-id=f4001f\na=rtpmap:47 AV1/90000\na=rtcp-fb:47 goog-remb\na=rtcp-fb:47 transport-cc\na=rtcp-fb:47 ccm fir\na=rtcp-fb:47 nack\na=rtcp-fb:47 nack pli\na=fmtp:47 level-idx=5;profile=1;tier=0\na=rtpmap:114 H264/90000\na=rtcp-fb:114 goog-remb\na=rtcp-fb:114 transport-cc\na=rtcp-fb:114 ccm fir\na=rtcp-fb:114 nack\na=rtcp-fb:114 nack pli\na=fmtp:114 level-asymmetry-allowed=1;packetization-mode=0;profile-level-id=64001f";
//
//    Properties properties = new Properties();
//
//    public JanusCall(String uniqueId,String aparty, String bparty,String secretKey) {
//        super(uniqueId,aparty,bparty);
//        websocket = new Websocket();
//        websocket.setCallEventListener(this);
//        websocket.initSocket(sipUserName);
//        this.callingNumber = aparty;
//        this.calledNumber = bparty;
//        this.secretKey = secretKey;
//
//    }
//
//
//    @Override
//    public void startCall() {
////        try (InputStream input = JanusCall.class.getClassLoader().getResourceAsStream("application.properties")) {
////            if (input == null) {
////                System.out.println("Sorry, unable to find application.properties");
////                return;
////            }
////            properties.load(input);
////        }
////        catch (IOException e) {
////            System.out.println("Sorry, unable to find application.properties");
////        }
//        startSession();
//        String publicIp = null;
//        try {
//            publicIp = getPublicIP();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        this.sipUserName = "sip:" + callingNumber + "@103.248.13.73";
//        this.receiver = "sip:" + publicIp + callingNumber + calledNumber + "@103.248.13.73";
//    }
//
//    @Override
//    public void onStart(Object message) {
//
//    }
//
//    @Override
//    public void onNewMessage(Object message) throws IOException {
//        JanusResponse messages = (JanusResponse) message;
//        String janusType = messages.getJanus();
//        switch (janusType) {
//            case "keepalive":
//                System.out.println("Got a keepalive on session " + sessionId);
//                break;
//            case "server_info":
//            case "success":
//                if (messages.getSessionId() == 0) {
//                    JanusResponse.Data = messages.getData();
//                    sessionId = JanusResponse.Data.getId();
//                    attachPlugin("janus.plugin.sip");
//                    System.out.println("Janus Connected " + sessionId);
//                } else {
//                    JanusResponse.Data = messages.getData();
//                    handleId = JanusResponse.Data.getId();
////                    registerToSIP(sipUserName, callingNumber , callingNumber , properties.getProperty("janus_secret"), "sip:103.248.13.73");
//                    registerToSIP(sipUserName, callingNumber , callingNumber , properties.getProperty("janus_secret"), "sip:103.248.13.73");
//                    websocket.startKeepAliveTimer();
//                }
//                System.out.println("Session Running... ");
//                break;
//            case "timeout": {
//                System.out.println("Time out....... ");
//                websocket.stopKeepAliveTimer();
//                websocket.closeSocket();
//            }
//            break;
//            case "event":
//                JanusResponse.plugin = messages.getPluginData();
//                if (JanusResponse.plugin.getData().getResult().getEvent().contains("registering")) {
//                    System.out.println("Registering...");
//                } else if (JanusResponse.plugin.getData().getResult().getEvent().contains("registered")) {
//                    System.out.println("Registered Success");
//                    actualCall(receiver, handleId, sessionId, "audio", sdp);
//                } else if (JanusResponse.plugin.getData().getResult().getEvent().contains("calling")) {
//                    System.out.println("Calling");
//
//                    //some works to do
//                } else if (JanusResponse.plugin.getData().getResult().getEvent().contains("ringing")) {
//                    System.out.println("ringing");
//
//                    //some works to do
//                } else if (JanusResponse.plugin.getData().getResult().getEvent().contains("proceeding")) {
//                    System.out.println("proceeding");
//
//                    //some works to do
//                } else if (JanusResponse.plugin.getData().getResult().getEvent().contains("registration_failed")) {
//
//                    System.out.println("registration_failed" + message.toString());
//                    websocket.stopKeepAliveTimer();
//                    websocket.closeSocket();
//                } else if (JanusResponse.plugin.getData().getResult().getEvent().contains("accepted")) {
//
//                    if (messages.getJsep().getSdp() != null) {
//                        System.out.println("Got answer SDP");
//
//                        JanusMessage.Jsep = messages.getJsep();
//                        //set Remote SDP HERE
//
//                    } else {
//
//                    }
//                } else if (JanusResponse.plugin.getData().getResult().getEvent().contains("progress")
//                ) {
//                    if (messages.getJsep().getSdp() != null) {
//                        System.out.println("Got answer SDP");
//
//                        JanusMessage.Jsep = messages.getJsep();
//                        //set Remote SDP HERE
//
//                    } else {
//                        System.out.println("No answer SDP");
//                    }
//                    //some works to do
//                } else if (JanusResponse.plugin.getData().getResult().getEvent().contains("updating")) {
//                    System.out.println("updating");
//                    JanusMessage.Jsep = messages.getJsep();
//                    //set Remote SDP HERE
//
//
//                } else {
//                    System.out.println("Some errors occur!");
//                }
//                break;
//            case "webrtcup":
//                break;
//
//            case "media":
//                System.out.println("media received");
//                break;
//            case "hangup":
//                System.out.println(message.toString());
//                websocket.stopKeepAliveTimer();
//                System.out.println("hangup");
//                websocket.closeSocket();
//                break;
//            case "ack":
//                System.out.println(message.toString());
//                break;
////            case "detached":
////                handleDetached(json);
////                break;
//
////            case "slowlink":
////                handleSlowLink(json);
////                break;
//            case "error":
//                System.out.println(message.toString());
//                break;
//
////            case "timeout":
////                handleTimeout(json);
////                break;
//            default:
//                System.out.println("Unknown message/event  '" + janusType + "' on session " + sessionId);
//                System.out.println(message.toString());
//        }
//
//    }
//
//    @Override
//    public void startSession(Object message) {
//
//    }
//
//    public void actualCall(String target, long handleId, long sessionId, String type, String sdp) {
//        sdp = "v=0\n" +
//                "o=- 4438716456674137629 2 IN IP4 127.0.0.1\n" +
//                "s=-\n" +
//                "t=0 0\n" +
//                "a=group:BUNDLE 0\n" +
//                "a=extmap-allow-mixed\n" +
//                "a=msid-semantic: WMS 68af45d7-57d3-4e24-b28c-43381bba67d3\n" +
//                "m=audio 9 UDP/TLS/RTP/SAVPF 111 63 9 0 8 13 110 126\n" +
//                "c=IN IP4 0.0.0.0\n" +
//                "a=rtcp:9 IN IP4 0.0.0.0\n" +
//                "a=ice-ufrag:cvc1\n" +
//                "a=ice-pwd:jf90ECDUOcqseeu4KG+4y50a\n" +
//                "a=ice-options:trickle\n" +
//                "a=fingerprint:sha-256 98:2B:E4:33:1C:71:B8:18:18:75:26:F8:04:30:34:86:49:3F:33:41:D8:EB:FE:99:59:69:0F:E0:4C:81:52:EA\n" +
//                "a=setup:actpass\n" +
//                "a=mid:0\n" +
//                "a=extmap:1 urn:ietf:params:rtp-hdrext:ssrc-audio-level\n" +
//                "a=extmap:2 http://www.webrtc.org/experiments/rtp-hdrext/abs-send-time\n" +
//                "a=extmap:3 http://www.ietf.org/id/draft-holmer-rmcat-transport-wide-cc-extensions-01\n" +
//                "a=extmap:4 urn:ietf:params:rtp-hdrext:sdes:mid\n" +
//                "a=sendrecv\n" +
//                "a=msid:68af45d7-57d3-4e24-b28c-43381bba67d3 0688c20c-5a51-4893-9086-7a05441e1194\n" +
//                "a=rtcp-mux\n" +
//                "a=rtcp-rsize\n" +
//                "a=rtpmap:111 opus/48000/2\n" +
//                "a=rtcp-fb:111 transport-cc\n" +
//                "a=fmtp:111 minptime=10;useinbandfec=1\n" +
//                "a=rtpmap:63 red/48000/2\n" +
//                "a=fmtp:63 111/111\n" +
//                "a=rtpmap:9 G722/8000\n" +
//                "a=rtpmap:0 PCMU/8000\n" +
//                "a=rtpmap:8 PCMA/8000\n" +
//                "a=rtpmap:13 CN/8000\n" +
//                "a=rtpmap:110 telephone-event/48000\n" +
//                "a=rtpmap:126 telephone-event/8000\n" +
//                "a=ssrc:647562650 cname:W8FC6OMttoOj0v/L\n" +
//                "a=ssrc:647562650 msid:68af45d7-57d3-4e24-b28c-43381bba67d3 0688c20c-5a51-4893-9086-7a05441e1194\n";
//        type = "offer";
//        JanusMessage.Body body = null;
//
////                        sdp = sdp.replace("opus/48000/2", "opus/16000/2");
//
//        sdp = sdp.replaceAll("(\\r)", "");
//        sdp = SDPParser.filterCodecs(sdp);
//        body = new JanusMessage.Body("call", target, false);
//
//        JanusMessage.Jsep jsep = new JanusMessage.Jsep(type, sdp);
//        JanusMessage message = new JanusMessage("message", body, TID(), jsep, sessionId, handleId);
//
//
//        // Get final JSON
//        String callMessageToJanus = null;
//        try {
//            callMessageToJanus = message.toJson(message);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(callMessageToJanus);
//        websocket.sendMessage(callMessageToJanus);
//    }
//
//    @Override
//    public void startSession() {
//        // Construct the JSON message for creating a session
//        String createSessionMessage = "{\"janus\":\"create\",\"transaction\":\"" + TID() + "\"}";
//        websocket.sendMessage(createSessionMessage);
//    }
//
//    @Override
//    public void updateSession() {
//
//    }
//
//    @Override
//    public void disconnect() {
//
//    }
//
//    @Override
//    public void onRing() {
//
//    }
//
//    @Override
//    public void onAnswer() {
//
//    }
//
//    Timer timer = new Timer("Timer");
//
//    public void startKeepAliveTimer() {
//        TimerTask task = new TimerTask() {
//            public void run() {
////                sendKeepAlive();
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
//
//    public void closeSocket() {
//        if (webSocket != null && webSocket.isOpen()) {
//            webSocket.close();
//        }
//    }
//
//    public void attachPlugin(String pluginName) {
//        // Construct the JSON message for attaching to a plugin
//        String attachMessage = "{\"janus\":\"attach\",\"plugin\":\"" + pluginName + "\",\"opaque_id\":\"" + "siptest-" + TID() + "\",\"transaction\":\"" + TID() + "\",\"session_id\":" + sessionId + "}";
//        websocket.sendMessage(attachMessage);
//    }
//
//    public void registerToSIP(String username, String authuser, String displayName, String secret, String proxy) {
//        // Construct the JSON message for registering to SIP
//        String registerMessage = "{\"janus\":\"message\",\"body\":{\"request\":\"register\",\"username\":\"" + username + "\",\"authuser\":\"" + authuser + "\",\"display_name\":\"" + displayName + "\",\"secret\":\"" + secret + "\",\"proxy\":\"" + proxy + "\"},\"transaction\":\"" + TID() + "\",\"session_id\":" + sessionId + ",\"handle_id\":" + handleId + "}";
//        websocket.sendMessage(registerMessage);
//    }
//
//}
//
//
