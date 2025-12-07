package com.comexapp.service;

/*
Responsável: Laura Pereira
A classe UsuarioService.java gerencia operações relacionadas a usuários, 
incluindo atualização de senhas e geração de senhas aleatórias seguras. 
Utiliza SecureRandom para criar senhas com caracteres alfanuméricos e 
especiais, garantindo maior segurança no processo de recuperação de senha.
*/

import com.comexapp.repository.UsuarioRepository;
import com.comexapp.model.Usuario;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class UsuarioService {

    // Repositório para persistência de usuários
    private final UsuarioRepository usuarioRepository;
    
    // Conjunto de caracteres permitidos para gerar senhas aleatórias
    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%!";

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Atualiza a senha de um usuário existente
    public void atualizarSenha(Long id, String novaSenha) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Atualiza o hash da senha (atenção: aqui está armazenando texto puro, não hash)
        usuario.setSenhaHash(novaSenha);
        usuarioRepository.save(usuario);
    }

    // Gera uma nova senha aleatória para o usuário e salva no banco
    public String gerarNovaSenha(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String senhaGerada = gerarSenhaAleatoria(10); // tamanho 10
        usuario.setSenhaHash(senhaGerada);
        usuarioRepository.save(usuario);

        return senhaGerada;
    }

    // Método auxiliar para gerar uma senha aleatória com caracteres definidos
    private String gerarSenhaAleatoria(int tamanho) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            int index = random.nextInt(CARACTERES.length());
            sb.append(CARACTERES.charAt(index));
        }
        return sb.toString();
    }
}
