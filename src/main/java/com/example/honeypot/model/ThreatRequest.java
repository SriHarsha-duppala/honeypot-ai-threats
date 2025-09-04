package com.example.honeypot.model;

public class ThreatRequest {
    private String ip;
    private String payload;

    public ThreatRequest() {}

    public ThreatRequest(String ip, String payload) {
        this.ip = ip;
        this.payload = payload;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
