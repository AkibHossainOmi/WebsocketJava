package com.tb.common.eventDriven;

import com.tb.common.ServiceEnum.PayloadType;

import java.util.HashMap;

public class Payload {
    public PayloadType getPayloadType() {
        return payloadType;
    }

    PayloadType payloadType;
    private String data; // Main payload data
    private HashMap<String, String> headers; // Additional protocol-specific headers
    private HashMap<String, Object> metadata; // Any extra metadata that might be needed

    public Payload(String data, PayloadType payloadType) {
        this.payloadType=payloadType;
        this.data = data;
        this.headers = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    // Getter and setter for data
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    // Getter and setter for headers
    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    // Getter and setter for metadata
    public HashMap<String, Object> getMetadata() {
        return metadata;
    }

    public void addMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    public Object getMetadata(String key) {
        return metadata.get(key);
    }

    @Override
    public String toString() {
        return "Payload{" +
                "data='" + data + '\'' +
                ", headers=" + headers +
                ", metadata=" + metadata +
                '}';
    }
}
