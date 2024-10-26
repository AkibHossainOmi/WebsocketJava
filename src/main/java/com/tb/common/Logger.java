package com.tb.common;

import com.tb.common.logging.LogSeverity;
import com.tb.common.logging.LoggerType;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private static String logFilePath = ""; // Default log file path is empty

    // Prevent instantiation
    private Logger() {}

    // Set the log file path
    public static void setLogFilePath(String filePath) {
        logFilePath = filePath;
    }

    // Log a message with a specified severity
    public static void log(LogSeverity severity, String message) {
        String logMessage = "[" + severity + "] " + message;
        System.out.println(logMessage); // Print to console
        writeToFile(logMessage);
    }

    // Log an exception with a specified severity
    public static void log(LogSeverity severity, Exception exception) {
        String logMessage = "[" + severity + "] " + exception.getMessage();
        System.out.println(logMessage); // Print to console
        writeToFile(logMessage);
        exception.printStackTrace(); // Print stack trace to console
    }

    // Log a message along with an exception
    public static void log(LogSeverity severity, String message, Exception exception) {
        String logMessage = "[" + severity + "] " + message;
        System.out.println(logMessage); // Print to console
        writeToFile(logMessage);
        exception.printStackTrace(); // Print stack trace to console
    }

    // Write log message to file if a file path is set
    private static void writeToFile(String logMessage) {
        if (logFilePath != null && !logFilePath.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
                writer.write(logMessage);
                writer.newLine(); // Add a new line
            } catch (IOException e) {
                System.err.println("Failed to write log to file: " + e.getMessage());
            }
        }
    }
}

