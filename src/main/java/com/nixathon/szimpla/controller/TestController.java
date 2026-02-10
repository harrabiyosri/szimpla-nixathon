package com.nixathon.szimpla.controller;

import com.nixathon.szimpla.service.ExternalApiService;
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

    private final ExternalApiService externalApiService;

    public TestController(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }

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

    @PostMapping("/process-tasks")
    public String processTasks() {

        externalApiService.processJson();

        return "Tasks processed successfully";
    }
}
