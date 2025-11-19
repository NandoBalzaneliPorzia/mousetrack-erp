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

    @Column(unique = true)
    private String codigo;
    private String titulo;
    private String tipo;    // importacao / exportacao
    private String modal;   // aereo / maritimo
    private String observacao;

    @Lob
    @Column(columnDefinition = "bytea")
    private byte[] arquivos;

    private String status = "Em andamento";
    private LocalDateTime dataCriacao = LocalDateTime.now();
}
