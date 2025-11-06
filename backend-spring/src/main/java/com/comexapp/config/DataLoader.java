package com.comexapp.config;

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

                // defina aqui a senha que você quer usar no login
                user.setSenhaHash(passwordEncoder.encode("123456"));

                usuarioRepository.save(user);
                System.out.println("✅ Usuário de teste criado: teste@empresa.com / 123456");
            }
        );
    }
}