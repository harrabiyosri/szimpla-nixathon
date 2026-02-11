package com.nixathon.szimpla.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class TestController {

    // Basic ping test
    @GetMapping("/healthz")
    public Map<String, Object> ping() {
        return Map.of(
                "status", "OK"
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
}
