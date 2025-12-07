package com.comexapp.config;

/*
Responsável: Nando Balzaneli
A classe CorsConfig.java é responsável por configurar o CORS (Cross-Origin Resource Sharing)
na aplicação Spring Boot. Ela define quais origens podem acessar a API, quais métodos HTTP
são permitidos, quais cabeçalhos podem ser usados, e permite o envio de credenciais.
Isso é importante para permitir a comunicação segura entre o frontend e o backend.
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // Cria e retorna um bean de configuração CORS
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Configura permissões de CORS para todas as rotas da aplicação
                registry.addMapping("/**")
                    .allowedOrigins(
                        "https://mousetrack-frontend.onrender.com", // frontend em produção
                        "http://localhost:8080",                     // frontend local padrão
                        "http://localhost:8088"                      // alternativa de porta local
                    )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // métodos permitidos
                    .allowedHeaders("*")         // todos os cabeçalhos são permitidos
                    .exposedHeaders("*")         // todos os cabeçalhos podem ser expostos ao cliente
                    .allowCredentials(true)      // permite envio de cookies/credenciais
                    .maxAge(3600);               // cache da configuração por 1 hora
            }
        };
    }
}
