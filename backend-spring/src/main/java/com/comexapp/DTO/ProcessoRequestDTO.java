
package com.comexapp.DTO;

import org.springframework.web.multipart.MultipartFile;

public class ProcessoRequestDTO {

    private String titulo;
    private String tipo;
    private String modal;
    private String observacao;
    private MultipartFile[] arquivos;

    // Getters e Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getModal() { return modal; }
    public void setModal(String modal) { this.modal = modal; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public MultipartFile[] getArquivos() { return arquivos; }
    public void setArquivos(MultipartFile[] arquivos) { this.arquivos = arquivos; }
}
