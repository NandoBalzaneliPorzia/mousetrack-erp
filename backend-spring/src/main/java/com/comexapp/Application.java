package com.comexapp;

/*
A classe Application.java é a classe principal da aplicação Spring Boot.
Responsável por inicializar e executar o projeto, realizando o scan dos
componentes (controllers, services, repositories, etc.) no pacote com.comexapp.
*/

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.comexapp")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
