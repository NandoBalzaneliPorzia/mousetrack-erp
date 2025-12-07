package com.comexapp.repository;

/*
Responsável: Nando Balzaneli
A interface ChatMessageRepository é um repositório Spring Data JPA para a 
entidade ChatMessage. Fornece métodos para persistência e consultas comuns,
como listar mensagens de uma thread, contar não lidas e obter a última mensagem.
*/

import com.comexapp.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Retorna todas as mensagens de uma thread em ordem cronológica
    List<ChatMessage> findByThreadIdOrderByEnviadoEmAsc(Long threadId);

    // Conta quantas mensagens de uma thread ainda não foram lidas
    long countByThreadIdAndLidoFalse(Long threadId);

    // Retorna a última mensagem de uma thread (para pré-visualização)
    ChatMessage findFirstByThreadIdOrderByEnviadoEmDesc(Long threadId);
}
