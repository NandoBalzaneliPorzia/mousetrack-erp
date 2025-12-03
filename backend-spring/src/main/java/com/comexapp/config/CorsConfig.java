package com.comexapp.config;

/*
A classe CorsConfig.java configura o CORS para a aplicação Spring Boot, 
permitindo que origens específicas (como o frontend e ambientes locais) 
acessem a API com métodos HTTP, cabeçalhos e credenciais controlados.
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    //criação e retorno do bean configurando as regras de cors  
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins(
                        "https://mousetrack-frontend.onrender.com",
                        "http://localhost:8080",
                        "http://localhost:8088"
                    )
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .exposedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);
            }
        };
    }
}
