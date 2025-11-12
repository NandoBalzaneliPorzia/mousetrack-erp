package com.comexapp.controller;

import com.comexapp.DTO.ProcessoRequestDTO;
import com.comexapp.model.Processo;
import com.comexapp.repository.ProcessoRepository;
import com.comexapp.service.ProcessoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/processos")
@CrossOrigin(origins = "https://mousetrack-frontend.onrender.com")
public class ProcessoController {

    private final ProcessoService service;
    private final ProcessoRepository repository;

    public ProcessoController(ProcessoService service, ProcessoRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Processo> criarProcesso(
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

        Processo created = service.criarProcesso(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public List<Processo> listarProcessos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Processo> getProcesso(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
