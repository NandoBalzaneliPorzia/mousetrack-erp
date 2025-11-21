package com.comexapp.repository;

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
