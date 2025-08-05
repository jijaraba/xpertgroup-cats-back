package com.xpertgroup.cats.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Health check controller for monitoring and preventing service sleep
 */
@RestController
public class HealthController {
    
    /**
     * Basic health check endpoint
     * @return Health status information
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "Cats API");
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(health);
    }
    
    /**
     * Simple ping endpoint
     * @return Pong response
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}