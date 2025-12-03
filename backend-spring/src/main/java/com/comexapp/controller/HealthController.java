package com.comexapp.controller;

/*
A classe HealthController.java é um controlador que fornece endpoints 
para verificar a saúde (health check) e a versão da aplicação. É útil 
para monitoramento e para garantir que o serviço está em execução e 
respondendo corretamente.
*/

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
