package com.example.honeypot.model;



public class ThreatResponse {
    private String result;

    public ThreatResponse() {}

    public ThreatResponse(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
