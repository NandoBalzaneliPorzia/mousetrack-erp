package com.comexapp.model;

import jakarta.persistence.*;

@Entity
public class ProcessoArquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String tipo;

    @Lob
    @Column(columnDefinition = "bytea")
    private byte[] dados;

    @ManyToOne
    @JoinColumn(name = "processo_id")
    private Processo processo;

    // GETTERS & SETTERS
}
