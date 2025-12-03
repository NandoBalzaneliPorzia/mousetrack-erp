package com.comexapp.controller;

/*
A classe TesteController.java é um controlador usado para testar a 
conectividade e o funcionamento básico do backend. Expõe um endpoint 
simples retornando uma mensagem de sucesso.
*/

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @GetMapping("/api/teste")
    public String teste() {
        return "Backend está funcionando ✅";
    }
}
