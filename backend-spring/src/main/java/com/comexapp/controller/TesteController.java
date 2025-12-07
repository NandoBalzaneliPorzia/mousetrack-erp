package com.comexapp.controller;

/*
Responsável: Ana Beatriz Maranho
A classe TesteController.java é um controlador REST simples utilizado para testar
se o backend está respondendo corretamente. Ele fornece um endpoint que retorna
uma mensagem de confirmação, útil para verificação rápida de conectividade.
*/

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Controlador REST para testes básicos de saúde do backend
@RestController
public class TesteController {

    // Endpoint de teste que retorna uma mensagem simples de sucesso
    @GetMapping("/api/teste")
    public String teste() {
        return "Backend está funcionando ✅";
    }
}
