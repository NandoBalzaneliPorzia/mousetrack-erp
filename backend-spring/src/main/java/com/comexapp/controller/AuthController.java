package com.comexapp.controller;

/*
A classe AuthController.java é um controlador REST responsável por receber 
requisições de login na rota /api/login, validar o email e a senha usando 
o AuthService e retornar, em caso de sucesso, o ID do usuário autenticado 
ou, em caso de falha, uma resposta 401 com mensagem de erro.
*/

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.comexapp.service.AuthService;
import com.comexapp.model.Usuario;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //implementação de endpoint de login na API REST
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String senha = payload.get("senha");

        System.out.println("📩 Requisição recebida no backend: " + email);

        Usuario usuario = authService.buscarPorEmail(email);

        if (usuario != null && authService.validarLogin(email, senha)) {
            // Retorna id e mensagem de sucesso
            return ResponseEntity.ok(Map.of(
                "mensagem", "Login realizado com sucesso!",
                "id", usuario.getId()
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of("erro", "Email ou senha inválidos"));
        }
    }
}
