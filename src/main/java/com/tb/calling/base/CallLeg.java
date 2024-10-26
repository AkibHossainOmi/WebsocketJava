package com.tb.calling.base;


public interface CallLeg {
    void startSession();
    void updateSession();
    void disconnect();
    void onRing();
    void onAnswer();
    void startRing();
    void answer();
    String extractSdp(String sdp);//json or xml
    String extractSdpIpAndPort(String sdp);//1.1.1.1:45676
}
