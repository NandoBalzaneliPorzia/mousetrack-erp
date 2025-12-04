    package com.comexapp.model;

    /* 
    A classe ProcessoArquivo.java representa um arquivo associado a um processo no sistema.
    Armazena os dados binários do arquivo, nome, tipo e data de criação, além de manter um 
    relacionamento com a entidade Processo.
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
        @JsonIgnore
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

        // Getters e Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getDataCriacao() { return dataCriacao; }
        public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }


        public byte[] getDadosArquivo() { return dadosArquivo; }
        public void setDadosArquivo(byte[] dadosArquivo) { this.dadosArquivo = dadosArquivo; }

        public String getNomeArquivo() { return nomeArquivo; }
        public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

        public String getTipoArquivo() { return tipoArquivo; }
        public void setTipoArquivo(String tipoArquivo) { this.tipoArquivo = tipoArquivo; }

        public Processo getProcesso() { return processo; }
        public void setProcesso(Processo processo) { this.processo = processo; }
    }
