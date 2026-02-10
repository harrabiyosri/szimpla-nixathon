package com.nixathon.szimpla.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    // Basic ping test
    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of(
                "status", "ok",
                "message", "backend is alive 🚀",
                "timestamp", Instant.now().toString()
        );
    }

    // Echo test for POST requests
    @PostMapping("/echo")
    public Map<String, Object> echo(@RequestBody Map<String, Object> body) {
        return Map.of(
                "received", body,
                "timestamp", Instant.now().toString()
        );
    }

    // Health check (good for Render monitoring)
    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}
