package com.comexapp.model;

/*
A classe ChatMessage.java representa uma mensagem de chat no sistema.
Esta entidade armazena detalhes como o ID da mensagem, a conversa 
(thread) à qual pertence, o autor da mensagem, o conteúdo, a data e 
hora de envio, e se a mensagem foi lida.
*/

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // conversa à qual a mensagem pertence
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    private ChatThread thread;

    // usuário que enviou
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "enviado_em")
    private LocalDateTime enviadoEm;

    @Column(name = "lido")
    private boolean lido;

    @PrePersist
    public void prePersist() {
        this.enviadoEm = LocalDateTime.now();
        this.lido = false;
    }

    // Getters e setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ChatThread getThread() { return thread; }
    public void setThread(ChatThread thread) { this.thread = thread; }

    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public LocalDateTime getEnviadoEm() { return enviadoEm; }
    public void setEnviadoEm(LocalDateTime enviadoEm) { this.enviadoEm = enviadoEm; }

    public boolean isLido() { return lido; }
    public void setLido(boolean lido) { this.lido = lido; }
}

