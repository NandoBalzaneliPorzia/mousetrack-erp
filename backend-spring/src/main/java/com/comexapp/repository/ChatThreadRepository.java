package com.comexapp.repository;

/*
Responsável: Nando Balzaneli
A interface ChatThreadRepository é um repositório Spring Data JPA para a 
entidade ChatThread. Fornece métodos para persistência e consultas de threads
de chat, incluindo a busca de uma thread por ID de processo.
*/

import com.comexapp.model.ChatThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatThreadRepository extends JpaRepository<ChatThread, Long> {

    // Retorna a primeira thread associada a um processo específico
    ChatThread findFirstByProcessoId(Long processoId);
}
