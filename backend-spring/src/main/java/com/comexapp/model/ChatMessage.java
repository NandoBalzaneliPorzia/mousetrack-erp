package com.comexapp.model;

/*
Responsável: Nando Balzaneli
A classe ChatMessage.java representa uma mensagem de chat no sistema.
Esta entidade é persistida no banco de dados e contém informações como:
- ID da mensagem
- Conversa (thread) à qual a mensagem pertence
- Usuário autor da mensagem
- Conteúdo da mensagem
- Data e hora de envio
- Status de leitura
*/

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relação Many-to-One com ChatThread (thread da mensagem)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    private ChatThread thread;

    // Relação Many-to-One com Usuario (autor da mensagem)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = true)
    private Usuario autor; // pode ser null para convidados

    @Column(name = "autor_guest")
    private String autorGuest; // nome do convidado

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "enviado_em")
    private LocalDateTime enviadoEm;

    @Column(name = "lido")
    private boolean lido;

    // Configura valores padrão antes de persistir no banco
    @PrePersist
    public void prePersist() {
        this.enviadoEm = LocalDateTime.now(); // define data/hora de envio atual
        this.lido = false;                   // marca como não lida
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ChatThread getThread() { return thread; }
    public void setThread(ChatThread thread) { this.thread = thread; }

    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }

    public String getAutorGuest() { return autorGuest; }
    public void setAutorGuest(String autorGuest) { this.autorGuest = autorGuest; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public LocalDateTime getEnviadoEm() { return enviadoEm; }
    public void setEnviadoEm(LocalDateTime enviadoEm) { this.enviadoEm = enviadoEm; }

    public boolean isLido() { return lido; }
    public void setLido(boolean lido) { this.lido = lido; }
}
