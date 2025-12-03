package com.comexapp.repository;

/*
A classe UsuarioRepository.java é um repositório que gerencia a entidade 
Usuario no banco de dados, fornecendo operações de persistência e consulta 
de usuários. Inclui funcionalidade para buscar um usuário específico pelo 
seu email.
*/

import com.comexapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
