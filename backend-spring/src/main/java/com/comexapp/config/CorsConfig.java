package com.comexapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // 👉 ORIGENS PERMITIDAS (sem "*")
                        .allowedOrigins(
                                "http://localhost:8080",           // backend direto (se acessar via browser)
                                "http://localhost:8088",           // seu proxy que serve o frontend
                                "http://127.0.0.1:8080",
                                "http://127.0.0.1:8088",
                                "https://mousetrack-frontend.onrender.com"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true); // ok, agora sem wildcard "*"
            }
        };
    }
}
