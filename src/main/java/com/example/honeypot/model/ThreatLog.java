package com.example.honeypot.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ThreatLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private String payload;
    private String result;
    private LocalDateTime timestamp;

    public ThreatLog() {}

    public ThreatLog(String ip, String payload, String result) {
        this.ip = ip;
        this.payload = payload;
        this.result = result;
        
    }
    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

    // Getters and setters
}
