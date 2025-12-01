package com.comexapp.repository;

import com.comexapp.model.ChatThread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatThreadRepository extends JpaRepository<ChatThread, Long> {

    ChatThread findFirstByProcessoId(Long processoId);
}
