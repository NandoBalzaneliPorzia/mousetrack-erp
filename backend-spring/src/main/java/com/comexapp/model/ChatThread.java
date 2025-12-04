package com.comexapp.model;

/*
A classe ChatThread.java representa uma thread de chat dentro do sistema.
Cada thread está associada a um processo e contém informações como:
- título da thread
- datas de criação e atualização
- lista de mensagens associadas
*/

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.comexapp.model.Processo;

@Entity
@Table(name = "chat_threads")
public class ChatThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome que aparece na lista (ex.: "Juliana Prado")
    @Column(nullable = false)
    private String titulo;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    // Relação One-to-Many com ChatMessage
    // Cascade ALL garante persistência e remoção em cascata
    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> mensagens;

    // Define datas automaticamente ao criar a thread
    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = this.criadoEm;
    }

    // Atualiza a data de atualização sempre que houver alterações
    @PreUpdate
    public void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }

    // Relação Many-to-One com Processo (thread associada a um processo)
    @ManyToOne
    @JoinColumn(name = "processo_id")
    private Processo processo;

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }

    public List<ChatMessage> getMensagens() { return mensagens; }
    public void setMensagens(List<ChatMessage> mensagens) { this.mensagens = mensagens; }

    public Processo getProcesso() { return processo; }
    public void setProcesso(Processo processo) { this.processo = processo; }
}
