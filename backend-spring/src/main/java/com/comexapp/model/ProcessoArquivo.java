package com.comexapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "processo_arquivo")
public class ProcessoArquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @Column(name = "tipo_arquivo")
    private String tipoArquivo;

    @Lob
    @Column(name = "dados_arquivo", columnDefinition = "bytea")
    private byte[] dadosArquivo;

    @ManyToOne
    @JoinColumn(name = "processo_id")
    private Processo processo;

    // =======================
    // GETTERS E SETTERS
    // =======================

    public Long getId() {
        return id;
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

    public byte[] getDadosArquivo() {
        return dadosArquivo;
    }

    public void setDadosArquivo(byte[] dadosArquivo) {
        this.dadosArquivo = dadosArquivo;
    }

    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }
}
