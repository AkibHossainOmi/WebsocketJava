package com.tb.common.eventDriven.RequestAndResponse.Enums;

import com.tb.common.eventDriven.RequestAndResponse.PayloadType;

public enum VertoPacket implements PayloadType {
    Login,
    Ping,
    PingResp,
    LoginResp,
    Invite,
    Modify,
    Ringing,
    Answer,
    Hangup
}

