package com.tb.calling.verto.msgTemplates;

public class Hangup2 {
    public static String createMessage(String callId,int id) {
        return """
                {
                    "jsonrpc": "2.0",
                    "method": "verto.bye",
                    "params": {
                        "dialogParams": {
                            "useVideo": false,
                            "useStereo": false,
                            "screenShare": false,
                            "useCamera": "any",
                            "useMic": "any",
                            "useSpeak": "any",
                            "tag": "webcam",
                            "localTag": null,
                            "login": "",
                            "videoParams": {
                                "minWidth": "",
                                "minHeight": "",
                                "minFrameRate": 15
                            },
                            "destination_number": "",
                            "caller_id_name": "",
                            "caller_id_number": "",
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
                                "answer_path": "",
                                "media_path": ""
                            },
                            "callID": "%s",
                            "remote_caller_id_name": "",
                            "remote_caller_id_number": ""
                        },
                        "sessid": ""
                    },
                    "id": %d
                }
            """.formatted(callId,id); // Using String#formatted to inject the serial number
    }
}
