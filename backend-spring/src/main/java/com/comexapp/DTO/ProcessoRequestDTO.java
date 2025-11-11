package com.comexapp.DTO;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class ProcessoRequestDTO {
    private String titulo;
    private String tipo;
    private String modal;
    private String observacao;
    private MultipartFile[] arquivos;
}
