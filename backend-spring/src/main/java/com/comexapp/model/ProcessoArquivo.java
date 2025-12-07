package com.comexapp.model;

/* 
Responsável: Juliana Prado
A classe ProcessoArquivo.java representa um arquivo associado a um processo no sistema.
Ela armazena informações do arquivo, como dados binários, nome, tipo e data de criação,
e mantém o relacionamento com a entidade Processo.

Campos principais:
- dadosArquivo: conteúdo binário do arquivo (Lob)
- nomeArquivo: nome do arquivo
- tipoArquivo: tipo/mime do arquivo
- dataCriacao: data em que o arquivo foi criado
- processo: referência ao processo ao qual o arquivo pertence
*/

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "processo_arquivo")
public class ProcessoArquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "dados_arquivo", nullable = false)
    @JsonIgnore // não expõe os dados binários ao serializar JSON
    private byte[] dadosArquivo;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @Column(name = "data_criacao")
    private String dataCriacao;

    @Column(name = "tipo_arquivo")
    private String tipoArquivo;

    @ManyToOne
    @JoinColumn(name = "processo_id")
    private Processo processo;

    // ============================
    //  GETTERS E SETTERS
    // ============================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public byte[] getDadosArquivo() { return dadosArquivo; }
    public void setDadosArquivo(byte[] dadosArquivo) { this.dadosArquivo = dadosArquivo; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public String getTipoArquivo() { return tipoArquivo; }
    public void setTipoArquivo(String tipoArquivo) { this.tipoArquivo = tipoArquivo; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }

    public Processo getProcesso() { return processo; }
    public void setProcesso(Processo processo) { this.processo = processo; }
}
