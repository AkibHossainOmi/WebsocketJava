package com.tb.common.eventDriven.RequestAndResponse.Enums;
public enum CallState {
    IDLE,
    REACHABILITY_CHECK,
    SESSION_START,
    CALLER_SDP_RECEIVED,
    WAITING_RINGING,
    WAITING_ANSWER,
    TRYING,
    RINGING,
    ANSWERED,
    HANGUP
}
