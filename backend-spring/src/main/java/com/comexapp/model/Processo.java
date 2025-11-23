package com.comexapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigo;

    private String titulo;
    private String tipo;      // importacao / exportacao
    private String modal;     // aereo / maritimo
    private String observacao;

    // 🔥 Agora não existe mais coluna arquivos dentro de Processo
    // Ela virou uma relação 1:N com ProcessoArquivo
    @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProcessoArquivo> arquivos = new ArrayList<>();

    private String status = "Em andamento";
    private LocalDateTime dataCriacao = LocalDateTime.now();

    // GETTERS -----------------------------

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

    public List<ProcessoArquivo> getArquivos() {
        return arquivos;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    // SETTERS -----------------------------

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

    public void setArquivos(List<ProcessoArquivo> arquivos) {
        this.arquivos = arquivos;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
