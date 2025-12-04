package com.comexapp.controller;

/*
A classe HealthController.java é um controlador REST usado para verificar 
a saúde da aplicação e fornecer informações de versão. Permite monitoramento 
simples para confirmar que o backend está ativo e funcionando corretamente.
*/

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HealthController {

    // Endpoint raiz para teste rápido
    @GetMapping("/")
    public String root() {
        return "Backend OK - MousTrack";
    }

    // Endpoint simples de health check
    @GetMapping("/healthz")
    public String health() {
        return "ok";
    }

    // Health check da API retornando JSON
    @GetMapping("/api/_health")
    public Map<String, String> apiHealth() {
        return Map.of("ok", "true");
    }

    // Retorna informações de versão e timestamp da API
    @GetMapping("/api/_version")
    public Map<String, String> apiVersion() {
        return Map.of(
            "msg", "auth-bcrypt-ok",
            "time", java.time.OffsetDateTime.now().toString()
        );
    }
}
