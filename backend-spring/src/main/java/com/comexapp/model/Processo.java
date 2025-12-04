package com.comexapp.model;

/*
A classe Processo.java representa a entidade "Processo" no banco de dados,
contendo informações sobre um processo e seu relacionamento com arquivos.
*/

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "processo")
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String tipo;
    private String modal;
    private String observacao;

    @Column(name = "data_criacao")
    private String dataCriacao;    

    @Column(unique = true)
    private String codigo;

    private String responsavel;

    // ============================================================
    //  RELACIONAMENTO CORRIGIDO
    // ============================================================
    @OneToMany(
            mappedBy = "processo",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<ProcessoArquivo> arquivos = new ArrayList<>();

    @Transient
    private int quantidadeArquivos;

    public int getQuantidadeArquivos() {
        return arquivos != null ? arquivos.size() : 0;
    }

    // ============================================================
    //  GETTERS E SETTERS
    // ============================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getModal() { return modal; }
    public void setModal(String modal) { this.modal = modal; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public List<ProcessoArquivo> getArquivos() { return arquivos; }
    public void setArquivos(List<ProcessoArquivo> arquivos) { this.arquivos = arquivos; }
}
