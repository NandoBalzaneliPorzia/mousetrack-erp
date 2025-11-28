package com.comexapp.model;

import jakarta.persistence.*;
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
    private String codigo;

    // Um processo tem vários arquivos
    @OneToMany(
        mappedBy = "processo",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<ProcessoArquivo> arquivos = new ArrayList<>();

    // Getters e Setters
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

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public List<ProcessoArquivo> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<ProcessoArquivo> arquivos) {
        this.arquivos = arquivos;
    }
}
