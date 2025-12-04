package com.comexapp.model;

/*
A classe Usuario.java representa a entidade 'Usuario' no banco de dados.
Ela mapeia a tabela 'usuarios' e define os atributos de um usuário,
como ID, email, senha (hash), nome, telefone, tipo de usuário,
status de atividade, ID do cliente associado e data de criação.
*/

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "senha_hash", nullable = false)
    private String senhaHash;

    @Column(nullable = false)
    private String nome;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "tipo_usuario", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    @Column(name = "ativo")
    private boolean ativo = true;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    // ✅ Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
