package com.tb.calling.verto.msgTemplates;

public class UnHold {
    public static String createMessage(String login,String destinationNumber, String callerIdName, String callerIdNumber,String callId, String remoteCallerIdNumber,String sessionId,int id) {
        return """
                {
                    "jsonrpc": "2.0",
                    "method": "verto.modify",
                    "params": {
                        "action": "unhold",
                        "params": {},
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
                            "destination_number": "%s",
                            "caller_id_name": "%s",
                            "caller_id_number": "%s",
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
                            "remote_caller_id_number": "%s"
                        },
                        "sessid": "%s"
                    },
                    "id": %d
                }
            """.formatted(login,destinationNumber,callerIdName, callerIdNumber,callId,remoteCallerIdNumber,sessionId,id); // Using String#formatted to inject the serial number
    }
}
