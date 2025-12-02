package com.comexapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "processo_arquivo")
public class ProcessoArquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "dados_arquivo", nullable = false)
    private byte[] dadosArquivo;   // <- TEM QUE SER byte[]

    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @Column(name = "tipo_arquivo")
    private String tipoArquivo;

    @ManyToOne
    @JoinColumn(name = "processo_id")
    @JsonIgnore   // impede recursão infinita
    private Processo processo;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public byte[] getDadosArquivo() { return dadosArquivo; }
    public void setDadosArquivo(byte[] dadosArquivo) { this.dadosArquivo = dadosArquivo; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public String getTipoArquivo() { return tipoArquivo; }
    public void setTipoArquivo(String tipoArquivo) { this.tipoArquivo = tipoArquivo; }

    public Processo getProcesso() { return processo; }
    public void setProcesso(Processo processo) { this.processo = processo; }
}
