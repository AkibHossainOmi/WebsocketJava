package com.tb.calling;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;

public class CallStateTracker {
    public ConcurrentHashMap<String, CallInfo> getCallDictionary() {
        return callDictionary;
    }

    // HashMap to hold both the call state and the state change timestamp
    private final ConcurrentHashMap<String, CallInfo> callDictionary = new ConcurrentHashMap<>();

    // Add or update the state for a given call ID
    public void updateCallState(String callId, CallState state) {
        if (state==CallState.SESSION_START && callDictionary.get(callId)!=null)
            throw new RuntimeException("CallId must be unique for each new call");
        callDictionary.put(callId, new CallInfo(state, LocalDateTime.now()));
    }

    // Retrieve the current state for a call
    public CallState getCallState(String callId) {
        CallInfo info = callDictionary.get(callId);
        return info != null ? info.state : null;
    }

    // Get the timestamp of the last state change for a call
    public LocalDateTime getStateChangeTimestamp(String callId) {
        CallInfo info = callDictionary.get(callId);
        return info != null ? info.timestamp : null;
    }

    // Remove a call from the state tracker (e.g. when a call is hung up)
    public void removeCall(String callId) {
        callDictionary.remove(callId);
    }

    // Check if a call exists in the tracker
    public boolean containsCall(String callId) {
        return callDictionary.containsKey(callId);
    }

    // Nested class to hold both the call state and timestamp

}
