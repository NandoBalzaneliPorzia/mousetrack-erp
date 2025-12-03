package com.comexapp.repository;

/*
A clase ChatThreadRepository.java é uma interface que define um repositório 
para a entidade ChatThread, fornecendo métodos para operações de persistência 
e consulta de threads (conversas) de chat. Inclui funcionalidade para buscar 
uma thread associada a um processo específico.
*/

import com.comexapp.model.ChatThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatThreadRepository extends JpaRepository<ChatThread, Long> {

    ChatThread findFirstByProcessoId(Long processoId);
}
