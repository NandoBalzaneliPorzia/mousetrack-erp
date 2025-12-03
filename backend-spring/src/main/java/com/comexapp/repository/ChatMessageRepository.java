package com.comexapp.repository;

/*
A classe ChatMessageRepository é uma interface que define um repositório para a 
entidade ChatMessage, fornecendo métodos para operações de persistência e 
consulta de mensagens de chat. Inclui funcionalidades para buscar mensagens por 
ID de conversa em ordem cronológica, contar mensagens não lidas e obter a última 
mensagem de uma conversa.
*/

import com.comexapp.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Mensagens de uma conversa em ordem cronológica
    List<ChatMessage> findByThreadIdOrderByEnviadoEmAsc(Long threadId);

    // Quantas mensagens não lidas
    long countByThreadIdAndLidoFalse(Long threadId);

    // Última mensagem da conversa (pro preview)
    ChatMessage findFirstByThreadIdOrderByEnviadoEmDesc(Long threadId);
}
