package com.comexapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/")
    public String root() {
        return "Backend OK - MousTrack";
    }

    @GetMapping("/healthz")
    public String health() {
        return "ok";
    }

    // --- Acrescentar estes dois abaixo ---
    @GetMapping("/api/_health")
    public Map<String, String> apiHealth() {
        return Map.of("ok", "true");
    }

    @GetMapping("/api/_version")
    public Map<String, String> apiVersion() {
        return Map.of(
            "msg", "auth-bcrypt-ok",
            "time", java.time.OffsetDateTime.now().toString()
        );
    }
}
