package com.comexapp.DTO;

/*
A classe ProcessoArquivoDTO.java representa um DTO para informações de 
um arquivo de processo. Ela encapsula dados como ID, nome, tipo, data 
de criação e código do processo associado ao arquivo.
*/

import java.time.LocalDateTime;

public class ProcessoArquivoDTO {

    private Long id;
    private String nomeArquivo;
    private String tipoArquivo;
    private String dataCriacao;
    private String processoCodigo;

    //construtor spring data jpa - arquivo
    public ProcessoArquivoDTO(Long id, String nomeArquivo, String tipoArquivo, String dataCriacao, String processoCodigo) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.tipoArquivo = tipoArquivo;
        this.dataCriacao = dataCriacao;
        this.processoCodigo = processoCodigo;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }
    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }
    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }
    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getProcessoCodigo() {
        return processoCodigo;
    }
    public void setProcessoCodigo(String processoCodigo) {
        this.processoCodigo = processoCodigo;
    }
    
}
