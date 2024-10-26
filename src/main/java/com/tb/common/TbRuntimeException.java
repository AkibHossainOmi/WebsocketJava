package com.tb.common;

import com.tb.common.logging.LogSeverity;

public class TbRuntimeException extends RuntimeException {
    private final LogSeverity severity;

    public TbRuntimeException(String message, LogSeverity severity) {
        super(message);
        this.severity = severity;
    }

    public LogSeverity getSeverity() {
        return severity;
    }
}

