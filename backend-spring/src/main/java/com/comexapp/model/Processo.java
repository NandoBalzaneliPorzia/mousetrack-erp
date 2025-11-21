package com.comexapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigo;

    private String titulo;
    private String tipo;    // importacao / exportacao
    private String modal;   // aereo / maritimo
    private String observacao;

    @Lob
    @Column(columnDefinition = "bytea")
    private byte[] arquivos;

    private String status = "Em andamento";
    private LocalDateTime dataCriacao = LocalDateTime.now();

    // GETTERS

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getModal() {
        return modal;
    }

    public String getObservacao() {
        return observacao;
    }

    public byte[] getArquivos() {
        return arquivos;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    // SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setModal(String modal) {
        this.modal = modal;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setArquivos(byte[] arquivos) {
        this.arquivos = arquivos;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
