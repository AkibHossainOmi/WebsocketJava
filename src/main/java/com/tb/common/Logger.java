package com.tb.common;

import com.tb.common.logging.LogSeverity;
import com.tb.common.logging.LoggerType;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger instance;
    private LoggerType loggerType = LoggerType.CONSOLE.CONSOLE; // Default writer
    private PrintWriter fileWriter;

    // Private constructor to prevent instantiation
    private Logger() {
        // Initialize the file writer if needed
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void setWriter(LoggerType loggerType) {
        this.loggerType = loggerType;
        if (loggerType == LoggerType.FILE.FILE) {
            try {
                fileWriter = new PrintWriter(new FileWriter("log.txt", true)); // Append mode
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void log(String message, LogSeverity severity) {
        String formattedMessage = formatMessage(message, severity);
        writeLog(formattedMessage);
    }

    public void log(Exception exception, LogSeverity severity) {
        String stackTrace = getStackTrace(exception);
        String formattedMessage = formatMessage(stackTrace, severity);
        writeLog(formattedMessage);
    }

    private void writeLog(String message) {
        switch (loggerType) {
            case CONSOLE:
                System.out.println(message);
                break;
            case SYSLOG:
                // Implement syslog writing logic here
                break;
            case FILE:
                if (fileWriter != null) {
                    fileWriter.println(message);
                    fileWriter.flush(); // Ensure it's written to the file
                }
                break;
        }
    }

    private String formatMessage(String message, LogSeverity severity) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format("[%s] [%s] %s", timestamp, severity, message);
    }

    private String getStackTrace(Exception exception) {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement element : exception.getStackTrace()) {
            stackTrace.append(element.toString()).append("\n");
        }
        return stackTrace.toString();
    }
}
