package com.tb.verto.msgTemplates;

public class Login {
    int id = 0;
    public static String createMessage(  String login, String password,String socketUrl,String sessionId,int id) {
        return """
                {
                     "jsonrpc": "2.0",
                     "method": "login",
                     "params": {
                         "login": "%s",
                         "passwd": "%s",
                         "socketUrl": "%s",
                         "autoReconnect": true,
                         "loglevel": "debug",
                         "keepAlive": false,
                         "tag": "webcam",
                         "ringer_tag": "ringer",
                         "localTag": null,
                         "videoParams": {
                             "minWidth": "1280",
                             "minHeight": "720",
                             "minFrameRate": 5,
                             "maxFrameRate": 15
                         },
                         "audioParams": {
                             "googAutoGainControl": false,
                             "googNoiseSuppression": false,
                             "googHighpassFilter": false
                         },
                         "loginParams": {},
                         "deviceParams": {
                             "useCamera": "any",
                             "useMic": "any",
                             "useSpeak": "any",
                             "onResCheck": null
                         },
                         "userVariables": {},
                         "iceServers": false,
                         "ringSleep": 6000,
                         "sessid": "%s",
                         "useStream": null,
                         "useVdieo": false,
                         "ringFile": "d45bac4ad5cf9ed6c65c5dce3422b847.wav"
                     },
                     "id": %d
                 }
            """.formatted(login, password,socketUrl,sessionId,id);  // Correctly formatted the string
    }

}
