package com.comexapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

