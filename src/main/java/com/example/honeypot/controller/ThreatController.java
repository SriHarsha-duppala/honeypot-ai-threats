package com.example.honeypot.controller;

import com.example.honeypot.model.ThreatLog;
import com.example.honeypot.model.ThreatRequest;
import com.example.honeypot.model.ThreatResponse;
import com.example.honeypot.repository.ThreatLogRepository;
import com.example.honeypot.service.ThreatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("/api/threat")
@CrossOrigin(origins = "http://localhost:3000")
public class ThreatController {

    private final ThreatService threatService;
    private final ThreatLogRepository threatLogRepository;

    public ThreatController(ThreatService threatService, ThreatLogRepository threatLogRepository) {
        this.threatService = threatService;
        this.threatLogRepository = threatLogRepository;
    }

    @GetMapping("/")
    public String testAPI() {
        return "Honeypot Threat Analysis API is running!";
    }

//    @PostMapping("/analyze")
//    public ThreatResponse analyzeThreat(@RequestBody ThreatRequest request) {
//        String analysis = threatService.analyzeThreat(request);
//        return new ThreatResponse(analysis);
//    }

    @GetMapping("/home")
    public String home() {
        return "Welcome to Honeypot Threat Analyzer!";
    }

    @GetMapping("/logs")
    public List<ThreatLog> getAllLogs() {
        return threatLogRepository.findAll();
    }

    @GetMapping("/logs/search/by-result")

    public List<ThreatLog> searchLogs(@RequestParam String keyword) {
        return threatLogRepository.findByResultContainingIgnoreCase(keyword);
    }


    @DeleteMapping("/logs/{id}")
    public ResponseEntity<String> deleteLogById(@PathVariable Long id) {
        if (threatLogRepository.existsById(id)) {
            threatLogRepository.deleteById(id);
            return ResponseEntity.ok("Log with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Log with ID " + id + " not found.");
        }
    }

    @DeleteMapping("/logs")
    public ResponseEntity<String> deleteAllLogs() {
        threatLogRepository.deleteAll();
        return ResponseEntity.ok("All logs have been deleted.");
    }
    @PutMapping("/logs/{id}")
    public ResponseEntity<?> updateLog(@PathVariable Long id, @RequestBody ThreatLog updatedLog) {
        return threatLogRepository.findById(id)
                .map(existingLog -> {
                    existingLog.setIp(updatedLog.getIp());
                    existingLog.setPayload(updatedLog.getPayload());
                    existingLog.setResult(updatedLog.getResult());
                    existingLog.setTimestamp(updatedLog.getTimestamp());
                    threatLogRepository.save(existingLog);
                    return ResponseEntity.ok("Log with ID " + id + " updated successfully.");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Log with ID " + id + " not found."));
    }
    @PatchMapping("/logs/{id}")
    public ResponseEntity<String> partialUpdateLog(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return threatLogRepository.findById(id).map(existingLog -> {
            if (updates.containsKey("ip")) {
                existingLog.setIp((String) updates.get("ip"));
            }
            if (updates.containsKey("payload")) {
                existingLog.setPayload((String) updates.get("payload"));
            }
            if (updates.containsKey("result")) {
                existingLog.setResult((String) updates.get("result"));
            }

            threatLogRepository.save(existingLog);
            return ResponseEntity.ok("Log with ID " + id + " partially updated successfully.");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Log with ID " + id + " not found."));
    }
    @PostMapping("/analyze")
    public ThreatResponse analyzeThreat(@RequestBody ThreatRequest request) {
        String analysis = threatService.analyzeThreat(request);

        // Save the threat log
        ThreatLog log = new ThreatLog();
        log.setIp(request.getIp());
        log.setPayload(request.getPayload());
        log.setResult(analysis);
        log.setTimestamp(java.time.LocalDateTime.now());

        threatLogRepository.save(log);

        return new ThreatResponse(analysis);
    }
    @GetMapping("/logs/filter")
    public List<ThreatLog> filterLogs(@RequestParam String keyword) {
        return threatLogRepository.findByResultContainingIgnoreCase(keyword);
    }
    @GetMapping("/logs/page")
    public Page<ThreatLog> getLogsPage(@RequestParam int page, @RequestParam int size) {
        return threatLogRepository.findAll(PageRequest.of(page, size));
    }
    @GetMapping("/logs/sorted")
    public List<ThreatLog> getSortedLogs(@RequestParam String sortBy) {
        return threatLogRepository.findAll(Sort.by(sortBy));
    }
    @GetMapping("/logs/stats")
    public Map<String, Long> threatStats() {
        List<ThreatLog> allLogs = threatLogRepository.findAll();
        long malwareCount = allLogs.stream().filter(l -> l.getResult().contains("Malware")).count();
        long safeCount = allLogs.stream().filter(l -> l.getResult().contains("Safe")).count();
        Map<String, Long> stats = new HashMap<>();
        stats.put("Malware", malwareCount);
        stats.put("Safe", safeCount);
        return stats;
    }
   
    @GetMapping("/logs/search/keyword/{keyword}")
    public List<ThreatLog> searchLogsByKeyword(@PathVariable String keyword) {
        return threatLogRepository.findByResultContainingIgnoreCase(keyword);
    }


}
