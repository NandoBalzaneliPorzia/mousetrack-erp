package com.comexapp.service;

/*
A classe AuthService.java gerencia a autenticação de usuários no sistema.
Fornece funcionalidades para buscar usuários por email e validar credenciais 
de login, verificando se a senha digitada corresponde ao hash armazenado no 
banco de dados e se o usuário está ativo.
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

    public Usuario buscarPorEmail(String email) {
        Optional<Usuario> opt = usuarioRepository.findByEmail(email);
        return opt.orElse(null);
    }

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