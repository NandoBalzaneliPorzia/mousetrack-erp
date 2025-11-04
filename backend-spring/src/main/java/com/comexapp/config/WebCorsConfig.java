package com.comexapp.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebCorsConfig {
  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration cfg = new CorsConfiguration();
    cfg.setAllowCredentials(true);
    cfg.setAllowedOriginPatterns(Arrays.asList(
        "http://localhost:*",
        "http://127.0.0.1:*",
        "https://*.onrender.com",
        "https://*.vercel.app"
    ));
    cfg.addAllowedHeader("*");
    cfg.addAllowedMethod("*"); // GET, POST, PUT, DELETE, OPTIONS etc.

    UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
    src.registerCorsConfiguration("/**", cfg);
    return new CorsFilter(src);
  }
}

