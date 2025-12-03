package com.comexapp.repository;

/*
A classe ClienteRepository.java é um repositório que gerencia a entidade 
Cliente no banco de dados, oferecendo operações padrão de CRUD (criar, 
ler, atualizar e deletar) por meio da interface JpaRepository.
*/

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.comexapp.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // você pode adicionar métodos personalizados aqui no futuro
}
