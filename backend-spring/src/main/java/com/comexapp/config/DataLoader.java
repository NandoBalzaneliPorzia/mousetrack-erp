package com.comexapp.config;

/*
A classe DataLoader.java é responsável por executar ações durante a inicialização
da aplicação Spring Boot. Seu objetivo principal é garantir que um usuário de teste
padrão (admin) exista no banco de dados, criando-o caso ainda não tenha sido criado.
Isso facilita testes e acesso inicial à aplicação sem precisar criar manualmente um usuário.
*/

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.comexapp.model.Usuario;
import com.comexapp.model.TipoUsuario;
import com.comexapp.repository.UsuarioRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Método executado automaticamente na inicialização do Spring Boot
    // Verifica se o usuário de teste já existe; caso não exista, cria um usuário admin padrão
    @Override
    public void run(String... args) throws Exception {
        AtomicLong clienteIdCounter = new AtomicLong(1000);

        usuarioRepository.findByEmail("teste@empresa.com").ifPresentOrElse(
            u -> System.out.println("✅ Usuário de teste já existe"),
            () -> {
                Usuario user = new Usuario();
                user.setEmail("teste@empresa.com");
                user.setNome("Usuário Teste");
                user.setTipoUsuario(TipoUsuario.admin);
                user.setAtivo(true);
                user.setClienteId(clienteIdCounter.getAndIncrement());

                // Define a senha padrão para login
                user.setSenhaHash(passwordEncoder.encode("123456"));

                usuarioRepository.save(user);
                System.out.println("✅ Usuário de teste criado: teste@empresa.com / 123456");
            }
        );
    }
}
