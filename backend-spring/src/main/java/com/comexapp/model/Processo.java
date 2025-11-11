package com.comexapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String titulo;
    private String tipo;    // importacao / exportacao
    private String modal;   // aereo / maritimo
    private String observacao;

    private String arquivos; // nomes ou caminhos separados por vírgula

    private LocalDateTime dataCriacao = LocalDateTime.now();
}
