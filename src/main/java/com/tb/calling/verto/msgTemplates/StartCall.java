package com.tb.calling.verto.msgTemplates;

public class StartCall {
    //static String sdp = "v=0\r\no=- 5218656177681112368 2 IN IP4 192.168.0.1\r\ns=-\r\nt=0 0\r\na=group:BUNDLE 0\r\na=extmap-allow-mixed\r\na=msid-semantic: WMS 67b9f0be-265f-4c06-8fe7-dc1a0f8b6133\r\nm=audio 59554 UDP/TLS/RTP/SAVPF 111 63 9 0 8 13 110 126\r\nc=IN IP4 192.168.56.1\r\na=rtcp:9 IN IP4 0.0.0.0\r\na=candidate:375522706 1 udp 2122260223 192.168.56.1 59554 typ host generation 0 network-id 2\r\na=candidate:1135503036 1 udp 2122194687 192.168.0.135 59555 typ host generation 0 network-id 1 network-cost 10\r\na=candidate:1756227338 1 tcp 1518280447 192.168.56.1 9 typ host tcptype active generation 0 network-id 2\r\na=candidate:1029800996 1 tcp 1518214911 192.168.0.135 9 typ host tcptype active generation 0 network-id 1 network-cost 10\r\na=ice-ufrag:Fhbi\r\na=ice-pwd:IudbY5FK0ZRfBHHhaIwVxeRC\r\na=ice-options:trickle\r\na=fingerprint:sha-256 80:A8:E2:37:8D:7D:EC:60:C1:26:68:04:61:EE:C3:4B:E5:4C:52:0A:DD:E4:04:22:A9:CC:C6:A1:B2:9E:26:AF\r\na=setup:actpass\r\na=mid:0\r\na=extmap:1 urn:ietf:params:rtp-hdrext:ssrc-audio-level\r\na=extmap:2 http://www.webrtc.org/experiments/rtp-hdrext/abs-send-time\r\na=extmap:3 http://www.ietf.org/id/draft-holmer-rmcat-transport-wide-cc-extensions-01\r\na=extmap:4 urn:ietf:params:rtp-hdrext:sdes:mid\r\na=sendrecv\r\na=msid:67b9f0be-265f-4c06-8fe7-dc1a0f8b6133 a2279374-0ccb-4640-9f05-dd621545e0cd\r\na=rtcp-mux\r\na=rtcp-rsize\r\na=rtpmap:111 opus/48000/2\r\na=rtcp-fb:111 transport-cc\r\na=fmtp:111 minptime=10;useinbandfec=1;x-google-max-bitrate=2048;x-google-min-bitrate=1024;x-google-start-bitrate=1024\r\na=rtpmap:63 red/48000/2\r\na=fmtp:63 111/111;x-google-max-bitrate=2048;x-google-min-bitrate=1024;x-google-start-bitrate=1024\r\na=rtpmap:9 G722/8000\r\na=rtpmap:0 PCMU/8000\r\na=rtpmap:8 PCMA/8000\r\na=rtpmap:13 CN/8000\r\na=rtpmap:110 telephone-event/48000\r\na=rtpmap:126 telephone-event/8000\r\na=ssrc:892345777 cname:nyBL+g6j0iexWJbx\r\na=ssrc:892345777 msid:67b9f0be-265f-4c06-8fe7-dc1a0f8b6133 a2279374-0ccb-4640-9f05-dd621545e0cd\r\n";
    static String getSdp(String ip, int port){
        String sdp = "v=0\r\n"
                + "o=- 5218656177681112368 2 IN IP4 192.168.0.1\r\n"
                + "s=-\r\n"
                + "t=0 0\r\n"
                + "a=group:BUNDLE 0\r\n"
                + "a=extmap-allow-mixed\r\n"
                + "a=msid-semantic: WMS 67b9f0be-265f-4c06-8fe7-dc1a0f8b6133\r\n"
                + "m=audio 59554 UDP/TLS/RTP/SAVPF 0 111 63 9 8 13 110 126\r\n" // Prioritize PCMU (0)
                + "c=IN IP4 192.168.56.1\r\n"
                + "a=rtcp:9 IN IP4 0.0.0.0\r\n"
                + "a=candidate:1135503036 1 udp 2122194687 "+ ip +" " + port + " typ host generation 0 network-id 1 network-cost 10\r\n"
                + "a=ice-ufrag:Fhbi\r\n"
                + "a=ice-pwd:IudbY5FK0ZRfBHHhaIwVxeRC\r\n"
                + "a=ice-options:trickle\r\n"
                + "a=fingerprint:sha-256 80:A8:E2:37:8D:7D:EC:60:C1:26:68:04:61:EE:C3:4B:E5:4C:52:0A:DD:E4:04:22:A9:CC:C6:A1:B2:9E:26:AF\r\n"
                + "a=setup:actpass\r\n"
                + "a=mid:0\r\n"
                + "a=extmap:1 urn:ietf:params:rtp-hdrext:ssrc-audio-level\r\n"
                + "a=extmap:2 http://www.webrtc.org/experiments/rtp-hdrext/abs-send-time\r\n"
                + "a=extmap:3 http://www.ietf.org/id/draft-holmer-rmcat-transport-wide-cc-extensions-01\r\n"
                + "a=extmap:4 urn:ietf:params:rtp-hdrext:sdes:mid\r\n"
                + "a=sendrecv\r\n"
                + "a=msid:67b9f0be-265f-4c06-8fe7-dc1a0f8b6133 a2279374-0ccb-4640-9f05-dd621545e0cd\r\n"
                + "a=rtcp-mux\r\n"
                + "a=rtcp-rsize\r\n"
                + "a=rtpmap:0 PCMU/8000\r\n" // PCMU codec definition
                + "a=rtpmap:111 opus/48000/2\r\n"
                + "a=rtpmap:63 red/48000/2\r\n"
                + "a=rtpmap:9 G722/8000\r\n"
                + "a=rtpmap:8 PCMA/8000\r\n"
                + "a=rtpmap:13 CN/8000\r\n"
                + "a=rtpmap:110 telephone-event/48000\r\n"
                + "a=rtpmap:126 telephone-event/8000\r\n"
                + "a=ssrc:892345777 cname:nyBL+g6j0iexWJbx\r\n"
                + "a=ssrc:892345777 msid:67b9f0be-265f-4c06-8fe7-dc1a0f8b6133 a2279374-0ccb-4640-9f05-dd621545e0cd\r\n";
        return sdp;
    }

    public static String createMessage(String login,String callId,String sessionId,int id, String ip, int port) {
        return """
                {
                    "jsonrpc": "2.0",
                    "method": "verto.invite",
                    "params": {
                        "sdp": "%s",
                        "dialogParams": {
                            "useVideo": false,
                            "useStereo": false,
                            "screenShare": false,
                            "useCamera": "any",
                            "useMic": "any",
                            "useSpeak": "any",
                            "tag": "webcam",
                            "localTag": null,
                            "login": "%s",
                            "videoParams": {
                                "minWidth": "1280",
                                "minHeight": "720",
                                "minFrameRate": 15
                            },
                            "destination_number": "01743801850",
                            "caller_id_name": "09646888888",
                            "caller_id_number": "09646888888",
                            "videoBandwidth": {
                                "max": 2048,
                                "min": 1024,
                                "start": 1024
                            },
                            "deviceParams": {
                                "useMic": "any",
                                "useSpeak": "any",
                                "useCamera": "any"
                            },
                            "userVariables": {
                                "answer_path": "/tmp/answer.wav",
                                "media_path": "/tmp/media.wav"
                            },
                            "callID": "%s",
                            "remote_caller_id_name": "Outbound Call",
                            "remote_caller_id_number": "01743801850" 
                        },
                        "sessid": "%s"
                    },
                    "id": %d
                }
            """.formatted(getSdp(ip, port), login,callId,sessionId,id);
//            """.formatted(login,destinationNumber,callerIdName,callerIdNumber,callId,sessionId,remoteCallerIdNumber,id);
    }
}
