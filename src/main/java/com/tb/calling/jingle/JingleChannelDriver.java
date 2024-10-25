package com.tb.calling.jingle;

import com.tb.calling.AbstractCallLeg;
import com.tb.calling.jingle.JingleConnector;
import java.util.HashMap;

public class JingleChannelDriver {

    private final JingleConnector jingleConnector;
    private final HashMap<String, AbstractCallLeg> jingleCalls;

    public JingleChannelDriver(JingleConnector jingleConnector) {
        this.jingleConnector = jingleConnector;
        this.jingleCalls = new HashMap<>();
    }

    public void addJingleCall(String rawMessage) {
        // Logic to create a new AbstractCallLeg instance based on the incoming message
        // Extract necessary information from rawMessage and initialize the call leg
        AbstractCallLeg newCall = createCallLeg(rawMessage);

        if (newCall != null) {
            jingleCalls.put(newCall.getUniqueId(), newCall);
            // Start the session or perform additional logic as needed
            newCall.startSession();
        }
    }

    public void onRing(String rawMessage) {
        // Logic to handle ringing state
        String uniqueId = extractUniqueIdFromMessage(rawMessage);
        AbstractCallLeg callLeg = jingleCalls.get(uniqueId);
        if (callLeg != null) {
            callLeg.onRing();
        }
    }

    public void onAnswer(String rawMessage) {
        // Logic to handle answer state
        String uniqueId = extractUniqueIdFromMessage(rawMessage);
        AbstractCallLeg callLeg = jingleCalls.get(uniqueId);
        if (callLeg != null) {
            callLeg.onAnswer();
        }
    }

    private AbstractCallLeg createCallLeg(String rawMessage) {
        // Logic to create and return a new AbstractCallLeg instance based on the message content
        // This can include extracting relevant details from rawMessage to initialize the call leg
        return null; // Replace with actual implementation
    }

    private String extractUniqueIdFromMessage(String rawMessage) {
        // Logic to extract the unique ID from the raw message
        // This will depend on the structure of the incoming message
        return null; // Replace with actual implementation
    }
}
