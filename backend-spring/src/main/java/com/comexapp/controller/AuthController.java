package com.comexapp.controller;

/*
Responsável: Laura Pereira
A classe AuthController.java é um controlador REST responsável por gerenciar
as requisições de autenticação no sistema.

Funcionalidades:
- Recebe requisições de login na rota /api/login
- Valida email e senha utilizando AuthService
- Retorna ID do usuário e mensagem de sucesso em caso de login válido
- Retorna status 401 e mensagem de erro em caso de falha
*/

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.comexapp.service.AuthService;
import com.comexapp.model.Usuario;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    // Serviço responsável pelas operações de autenticação
    private final AuthService authService;

    // Construtor: injeta AuthService
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Endpoint POST /api/login
    // Recebe JSON com "email" e "senha"
    // Retorna 200 com ID do usuário se login válido
    // Retorna 401 se login inválido
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        // Extrai email e senha do corpo da requisição
        String email = payload.get("email");
        String senha = payload.get("senha");

        System.out.println("📩 Requisição recebida no backend: " + email);

        // Busca usuário no banco de dados pelo email
        Usuario usuario = authService.buscarPorEmail(email);

        // Valida email e senha
        if (usuario != null && authService.validarLogin(email, senha)) {
            // Login válido: retorna ID do usuário e mensagem
            return ResponseEntity.ok(Map.of(
                "mensagem", "Login realizado com sucesso!",
                "id", usuario.getId()
            ));
        } else {
            // Login inválido: retorna 401 e mensagem de erro
            return ResponseEntity.status(401)
                                 .body(Map.of("erro", "Email ou senha inválidos"));
        }
    }
}
