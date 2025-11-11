package com.comexapp.controller;

import com.comexapp.DTO.ProcessoRequestDTO;
import com.comexapp.model.Processo;
import com.comexapp.service.ProcessoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/processos")
public class ProcessoController {

    private final ProcessoService service;

    public ProcessoController(ProcessoService service) {
        this.service = service;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public Processo criarProcesso(
            @RequestParam String titulo,
            @RequestParam String tipo,
            @RequestParam String modal,
            @RequestParam(required = false) String observacao,
            @RequestParam(required = false) MultipartFile[] arquivos) {

        ProcessoRequestDTO dto = new ProcessoRequestDTO();
        dto.setTitulo(titulo);
        dto.setTipo(tipo);
        dto.setModal(modal);
        dto.setObservacao(observacao);
        dto.setArquivos(arquivos);

        return service.criarProcesso(dto);
    }
}
