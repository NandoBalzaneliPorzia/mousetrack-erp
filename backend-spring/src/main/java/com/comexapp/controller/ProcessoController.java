package com.comexapp.controller;

import com.comexapp.DTO.ProcessoRequestDTO;
import com.comexapp.model.Processo;
import com.comexapp.repository.ProcessoRepository;
import com.comexapp.service.ProcessoService;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/processos")
public class ProcessoController {

    private final ProcessoService service;
    private final ProcessoRepository repository;

    public ProcessoController(ProcessoService service, ProcessoRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> criarProcesso(
            @RequestParam String titulo,
            @RequestParam String tipo,
            @RequestParam String modal,
            @RequestParam(required = false) String observacao,
            @RequestParam(value = "arquivos", required = false) MultipartFile[] arquivos) {

        try {
            ProcessoRequestDTO dto = new ProcessoRequestDTO();
            dto.setTitulo(titulo);
            dto.setTipo(tipo);
            dto.setModal(modal);
            dto.setObservacao(observacao);
            dto.setArquivos(arquivos);

            Processo created = service.criarProcesso(dto);

            // Retorna o objeto criado como JSON (frontend espera json)
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            e.printStackTrace();
            // Retorna JSON com erro para que o frontend possa ler o texto
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao criar processo", "detail", e.getMessage()));
        }
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
