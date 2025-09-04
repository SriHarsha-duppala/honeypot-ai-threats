package com.example.honeypot.service;

import com.example.honeypot.model.ThreatLog;
import com.example.honeypot.model.ThreatRequest;
import com.example.honeypot.repository.ThreatLogRepository;
import org.springframework.stereotype.Service;

@Service
public class ThreatService {

    private final ThreatLogRepository repository;

    public ThreatService(ThreatLogRepository repository) {
        this.repository = repository;
    }

    public String analyzeThreat(ThreatRequest request) {
        String result;
        if (request.getPayload() == null || request.getPayload().isEmpty()) {
            result = "No payload provided. Threat analysis skipped.";
        } else if (request.getPayload().toLowerCase().contains("malware")) {
            result = "Threat Detected: Malware signature found in payload!";
        } else {
            result = "No significant threat detected for IP: " + request.getIp();
        }

        // Save to DB
        ThreatLog log = new ThreatLog(request.getIp(), request.getPayload(), result);
        repository.save(log);

        return result;
    }
}
