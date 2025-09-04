package com.example.honeypot.repository;

import com.example.honeypot.model.ThreatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ThreatLogRepository extends JpaRepository<ThreatLog, Long> {
    List<ThreatLog> findByIp(String ip);  // ✅ Add this method
   

    List<ThreatLog> findByResultContainingIgnoreCase(String keyword);

}
