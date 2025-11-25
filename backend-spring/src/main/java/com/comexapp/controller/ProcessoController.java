package com.comexapp.controller;

import com.comexapp.DTO.ProcessoRequestDTO;
import com.comexapp.model.Processo;
import com.comexapp.repository.ProcessoRepository;
import com.comexapp.service.ProcessoService;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/processos")
public class ProcessoController {

    private final ProcessoService service;
    private final ProcessoRepository repository;

    public ProcessoController(ProcessoService service, ProcessoRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> criarProcesso(@ModelAttribute ProcessoRequestDTO dto) {
        try {

            System.out.println("=== DEBUG RECEBIMENTO DE FORM-DATA ===");
            System.out.println("titulo: " + dto.getTitulo());
            System.out.println("tipo: " + dto.getTipo());
            System.out.println("modal: " + dto.getModal());
            System.out.println("observacao: " + dto.getObservacao());

            MultipartFile[] arquivos = dto.getArquivos();

            if (arquivos == null) {
                System.out.println("Nenhum arquivo recebido (arquivos == null)");
            } else {
                System.out.println("Qtd de arquivos recebidos: " + arquivos.length);
                for (MultipartFile f : arquivos) {
                    System.out.println(" - Nome: " + f.getOriginalFilename());
                    System.out.println("   ContentType: " + f.getContentType());
                    System.out.println("   Tamanho: " + f.getSize() + " bytes");
                }
            }

            Processo created = service.criarProcesso(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (Exception e) {
            e.printStackTrace();
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
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
