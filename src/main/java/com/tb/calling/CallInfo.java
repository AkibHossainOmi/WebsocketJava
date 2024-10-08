package com.tb.calling;

import java.time.LocalDateTime;

public class CallInfo {
    CallState state;
    public LocalDateTime timestamp;

    CallInfo(CallState state, LocalDateTime timestamp) {
        this.state = state;
        this.timestamp = timestamp;
    }
}