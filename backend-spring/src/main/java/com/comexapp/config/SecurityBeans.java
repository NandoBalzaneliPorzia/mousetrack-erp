package com.comexapp.config;

/*
A classe SecurityBeans.java fornece configurações de segurança específicas
para a aplicação Spring Boot. Atualmente, define um bean de PasswordEncoder
usando o algoritmo BCrypt, permitindo codificação segura de senhas antes de
armazená-las no banco de dados.
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeans {

    // Bean que fornece um codificador de senhas BCrypt para uso em toda a aplicação
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
