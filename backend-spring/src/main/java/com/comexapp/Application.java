package com.comexapp;

/*
Responsável: Nando Balzaneli
A classe Application.java é o ponto de entrada da aplicação Spring Boot.
Ela inicializa o contexto da aplicação, realiza o scan dos componentes 
(controllers, services, repositories, etc.) no pacote com.comexapp e 
inicia o servidor embutido.
*/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.comexapp")
public class Application {
    public static void main(String[] args) {
        // Inicializa e executa a aplicação Spring Boot
        SpringApplication.run(Application.class, args);
    }
}
