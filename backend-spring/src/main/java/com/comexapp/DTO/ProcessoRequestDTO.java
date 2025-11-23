package com.comexapp.DTO;

import org.springframework.web.multipart.MultipartFile;

public class ProcessoRequestDTO {

    private String titulo;
    private String tipo;
    private String modal;
    private String observacao;
    private MultipartFile arquivos;

    // GETTERS

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

    public MultipartFile[] getArquivos() {
        return arquivos;
    }

    // SETTERS

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

    public void setArquivos(MultipartFile[] arquivos) {
        this.arquivos = arquivos;
    }
}
