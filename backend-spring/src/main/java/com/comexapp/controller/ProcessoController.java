package com.comexapp.controller;

import com.comexapp.DTO.ProcessoRequestDTO;
import com.comexapp.DTO.ProcessoResponseDTO;
import com.comexapp.DTO.ProcessoArquivoDTO;
import com.comexapp.model.Processo;
import com.comexapp.model.ProcessoArquivo;
import com.comexapp.repository.ProcessoRepository;
import com.comexapp.repository.ProcessoArquivoRepository;
import com.comexapp.service.ProcessoService;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/processos")
public class ProcessoController {

    private final ProcessoService service;
    private final ProcessoRepository repository;
    private final ProcessoArquivoRepository arquivoRepo;

    public ProcessoController(
            ProcessoService service,
            ProcessoRepository repository,
            ProcessoArquivoRepository arquivoRepo
    ) {
        this.service = service;
        this.repository = repository;
        this.arquivoRepo = arquivoRepo;
    }

    // ================================
    //   CRIAR PROCESSO COM ARQUIVOS
    // ================================
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> criarProcesso(
            @RequestPart("titulo") String titulo,
            @RequestPart("tipo") String tipo,
            @RequestPart("modal") String modal,
            @RequestPart(value = "observacao", required = false) String observacao,
            @RequestPart(value = "arquivos", required = false) MultipartFile[] arquivos
    ) {
        try {
            ProcessoRequestDTO dto = new ProcessoRequestDTO();
            dto.setTitulo(titulo);
            dto.setTipo(tipo);
            dto.setModal(modal);
            dto.setObservacao(observacao);
            dto.setArquivos(arquivos);

            Processo created = service.criarProcesso(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao criar processo", "detail", e.getMessage()));
        }
    }

    // ================================
    //   UPLOAD DE ARQUIVOS AO CARD
    // ================================
    @PostMapping(value = "/{codigo}/arquivos", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadArquivos(
            @PathVariable String codigo,
            @RequestPart("arquivos") MultipartFile[] arquivos
    ) {
        try {
            service.salvarArquivosNoProcesso(codigo, arquivos);

            return ResponseEntity.ok(Map.of(
                    "message", "Arquivos enviados com sucesso!",
                    "count", arquivos.length
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Falha ao anexar arquivos", "detail", e.getMessage()));
        }
    }

    // ================================
    //   LISTAR PROCESSOS
    // ================================
    @GetMapping
    public List<ProcessoResponseDTO> listarProcessos() {
        return repository.findAll().stream().map(p -> {
            ProcessoResponseDTO dto = new ProcessoResponseDTO();
            dto.setId(p.getId());
            dto.setCodigo(p.getCodigo());
            dto.setTitulo(p.getTitulo());
            dto.setTipo(p.getTipo());
            dto.setModal(p.getModal());
            dto.setObservacao(p.getObservacao());
            dto.setResponsavel(p.getResponsavel());
            dto.setDataCriacao(p.getDataCriacao());
            dto.setQuantidadeArquivos(arquivoRepo.countByProcessoId(p.getId()));
            return dto;
        }).toList();
    }

    // ================================
    //   BUSCAR PROCESSO POR ID
    // ================================
    @GetMapping("/{id}")
    public ResponseEntity<ProcessoResponseDTO> getProcesso(@PathVariable Long id) {
        return repository.findById(id)
                .map(p -> {
                    ProcessoResponseDTO dto = new ProcessoResponseDTO();
                    dto.setId(p.getId());
                    dto.setCodigo(p.getCodigo());
                    dto.setTitulo(p.getTitulo());
                    dto.setTipo(p.getTipo());
                    dto.setModal(p.getModal());
                    dto.setObservacao(p.getObservacao());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================================
    //   LISTAR ARQUIVOS DO PROCESSO (SEM LOB)
    // ================================
    @GetMapping("/{codigo}/arquivos")
    public ResponseEntity<?> listarArquivos(@PathVariable String codigo) {
        try {
            List<ProcessoArquivoDTO> dtos = arquivoRepo.findDTOByProcessoCodigo(codigo);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // ================================
    //   DOWNLOAD DE ARQUIVO
    // ================================
    @GetMapping("/download/{idArquivo}")
    public ResponseEntity<?> downloadArquivo(@PathVariable Long idArquivo) {
        try {
            ProcessoArquivo arq = arquivoRepo.findById(idArquivo)
                    .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + arq.getNomeArquivo() + "\"")
                    .body(arq.getDadosArquivo());

        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // ================================
    //   LISTAR TODOS OS ARQUIVOS DO REPOSITÓRIO
    // ================================
    @GetMapping("/repositorio")
    public ResponseEntity<?> listarPastasRepositorio() {
        try {
            List<ProcessoArquivo> arquivos = arquivoRepo.findAll();

            List<ProcessoArquivoDTO> dtos = arquivos.stream().map(a ->
                new ProcessoArquivoDTO(
                    a.getId(),
                    a.getNomeArquivo(),
                    a.getTipoArquivo(),
                    a.getDataCriacao(),
                    a.getProcesso().getCodigo()
                )
            ).toList();

            return ResponseEntity.ok(dtos);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }
    // ================================
    //   endpoint de busca de processo por codigo
    // ================================

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProcessoResponseDTO> getProcessoPorCodigo(@PathVariable String codigo) {
        return repository.findByCodigo(codigo)
                .map(p -> {
                    ProcessoResponseDTO dto = new ProcessoResponseDTO();
                    dto.setId(p.getId());
                    dto.setCodigo(p.getCodigo());
                    dto.setTitulo(p.getTitulo());
                    dto.setTipo(p.getTipo());
                    dto.setModal(p.getModal());
                    dto.setObservacao(p.getObservacao());
                    dto.setResponsavel(p.getResponsavel());
                    dto.setDataCriacao(p.getDataCriacao());
                    dto.setQuantidadeArquivos(arquivoRepo.countByProcessoId(p.getId()));
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}