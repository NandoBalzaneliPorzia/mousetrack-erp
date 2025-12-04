package com.comexapp.DTO;

/*
A classe ProcessoArquivoDTO.java é um DTO (Data Transfer Object) usado para 
representar informações de arquivos associados a um processo. Contém os dados 
principais do arquivo sem carregar o conteúdo binário (LOB), facilitando a 
transferência de informações entre backend e frontend. Campos principais:
- id: identificador do arquivo
- nomeArquivo: nome do arquivo
- tipoArquivo: tipo MIME do arquivo
- dataCriacao: data em que o arquivo foi criado
- processoCodigo: código do processo associado ao arquivo
*/
import java.time.LocalDateTime;

public class ProcessoArquivoDTO {

    private Long id;
    private String nomeArquivo;
    private String tipoArquivo;
    private String dataCriacao;
    private String processoCodigo;

    // Construtor para criação do DTO a partir do banco de dados
    public ProcessoArquivoDTO(Long id, String nomeArquivo, String tipoArquivo, String dataCriacao, String processoCodigo) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.tipoArquivo = tipoArquivo;
        this.dataCriacao = dataCriacao;
        this.processoCodigo = processoCodigo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public String getTipoArquivo() { return tipoArquivo; }
    public void setTipoArquivo(String tipoArquivo) { this.tipoArquivo = tipoArquivo; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getProcessoCodigo() { return processoCodigo; }
    public void setProcessoCodigo(String processoCodigo) { this.processoCodigo = processoCodigo; }
}
