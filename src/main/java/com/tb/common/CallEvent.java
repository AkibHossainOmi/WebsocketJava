package com.tb.common;

import com.tb.common.eventDriven.RequestAndResponse.Enums.CallEventType;

import java.util.HashMap;

public class CallEvent {
    public CallEventType getEventType() {
        return messageType;
    }

    private final CallEventType messageType;
    private final String sessionId;

    public String getRawData() {
        return rawData;
    }

    private final String rawData;

    public HashMap<String, Object> getMetaData() {
        return metaData;
    }

    private final HashMap<String, Object> metaData= new HashMap<>();
    public CallEvent(CallEventType messageType, String sessionId, String rawData) {
        this.messageType = messageType;
        this.sessionId = sessionId;
        this.rawData = rawData;
    }
    private String aParty;                // The identifier for party A (caller)
    private String bParty;                // The identifier for party B (callee)

    public String getaPartyDeviceId() {
        return aPartyDeviceId;
    }

    public void setaPartyDeviceId(String aPartyDeviceId) {
        this.aPartyDeviceId = aPartyDeviceId;
    }

    public String getbPartyDeviceId() {
        return bPartyDeviceId;
    }

    public void setbPartyDeviceId(String bPartyDeviceId) {
        this.bPartyDeviceId = bPartyDeviceId;
    }

    private String aPartyDeviceId;
    private String bPartyDeviceId;
    private String sourceDomain;          // Domain of the source party
    private String destinationDomain;     // Domain of the destination party
    private String codec;                 // Codec used for the call (e.g., "opus", "G711")
    private int pTime;                    // Packet time for the media stream
    private String callId;                // Unique identifier for the call
    private String timestamp;              // Timestamp of the message
    private String correlationId;         // ID for tracking related messages
    private String mediaType;              // Type of media (e.g., "audio", "video")
    private String status;                 // Status of the call (e.g., "ringing", "answered", "ended")

    // Constructor


    // Getters and Setters
    public String getaParty() {
        return aParty;
    }

    public void setaParty(String aParty) {
        this.aParty = aParty;
    }

    public String getbParty() {
        return bParty;
    }

    public void setbParty(String bParty) {
        this.bParty = bParty;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSourceDomain() {
        return sourceDomain;
    }

    public void setSourceDomain(String sourceDomain) {
        this.sourceDomain = sourceDomain;
    }

    public String getDestinationDomain() {
        return destinationDomain;
    }

    public void setDestinationDomain(String destinationDomain) {
        this.destinationDomain = destinationDomain;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public int getPTime() {
        return pTime;
    }

    public void setPTime(int pTime) {
        this.pTime = pTime;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
