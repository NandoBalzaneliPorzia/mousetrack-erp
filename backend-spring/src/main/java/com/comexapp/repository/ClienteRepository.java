package com.comexapp.repository;

/*
A interface ClienteRepository é um repositório Spring Data JPA para a entidade Cliente.
Permite realizar operações CRUD no banco de dados, como salvar, buscar, atualizar e deletar clientes.
*/

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.comexapp.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Possibilidade de adicionar métodos personalizados no futuro
}
