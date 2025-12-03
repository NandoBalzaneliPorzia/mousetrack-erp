package com.comexapp.config;

/*
A classe DataLoader.java é uma classe de configuração que roda na inicialização 
da aplicação para garantir a existência de um usuário de teste padrão no banco 
de dados, criando-o com e-mail, senha e permissões pré-definidos caso ainda não 
exista.
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

    //método executado automaticamente quando a aplicação Spring Boot inicia
    //resumindo, garante que um usuário admin padrão para fazer login e testar a aplicação
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

                // definição da senha para login 
                user.setSenhaHash(passwordEncoder.encode("123456"));

                usuarioRepository.save(user);
                System.out.println("✅ Usuário de teste criado: teste@empresa.com / 123456");
            }
        );
    }
}