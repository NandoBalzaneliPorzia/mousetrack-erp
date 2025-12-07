package com.comexapp.service;

/*
Responsável: Laura Pereira
A classe AuthService.java é responsável por gerenciar a autenticação de usuários.
Ela fornece métodos para:
- Buscar um usuário pelo email
- Validar login comparando a senha digitada com o hash armazenado
- Verificar se o usuário está ativo
*/

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.comexapp.model.Usuario;
import com.comexapp.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Busca um usuário pelo email
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    // Valida o login comparando a senha digitada com o hash e verifica se o usuário está ativo
    public boolean validarLogin(String email, String senhaDigitada) {
        Optional<Usuario> opt = usuarioRepository.findByEmail(email);
        if (opt.isEmpty()) {
            System.out.println("❌ Usuário não encontrado.");
            return false;
        }

        Usuario user = opt.get();
        System.out.println("Senha hash no banco: " + user.getSenhaHash());

        boolean senhaConfere = passwordEncoder.matches(senhaDigitada, user.getSenhaHash());
        System.out.println("🔑 Senha válida? " + senhaConfere);

        return user.isAtivo() && senhaConfere;
    }
}
