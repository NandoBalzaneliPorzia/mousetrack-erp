package com.comexapp.repository;

/*
Responsável: Laura Pereira
A interface UsuarioRepository é um repositório Spring Data JPA para a entidade Usuario.
Ela fornece operações padrão de CRUD e métodos customizados, incluindo:
- Busca de um usuário pelo email
*/

import com.comexapp.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca um usuário pelo email, retornando um Optional para lidar com ausência
    Optional<Usuario> findByEmail(String email);
}
