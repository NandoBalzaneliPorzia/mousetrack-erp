package com.comexapp.config;

/*
A classe SecurityBeans.java define beans relacionados à segurança da 
aplicação, como o codificador de senhas (PasswordEncoder) usando BCrypt.
*/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeans {
    //método codificador de senhas para a aplicação Spring
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

