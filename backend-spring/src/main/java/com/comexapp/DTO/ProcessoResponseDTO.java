package com.comexapp.DTO;

public class ProcessoResponseDTO {

    private Long id;
    private String codigo;
    private String titulo;
    private String tipo;
    private String modal;
    private String observacao;
    private String responsavel;
    private String dataCriacao;
    private int quantidadeArquivos;

    // Getters e Setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getModal() {
        return modal;
    }
    public void setModal(String modal) {
        this.modal = modal;
    }

    public String getObservacao() {
        return observacao;
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getResponsavel() {
        return responsavel;
    }
    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public int getQuantidadeArquivos() {
        return quantidadeArquivos;
    }
    public void setQuantidadeArquivos(int quantidadeArquivos) {
        this.quantidadeArquivos = quantidadeArquivos;
    }
}
