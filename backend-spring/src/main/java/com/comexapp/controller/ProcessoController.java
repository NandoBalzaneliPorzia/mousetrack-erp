package com.comexapp.controller;

/*
A classe ProcessoController.java é um controlador REST responsável por gerenciar 
todas as operações relacionadas à entidade Processo. Ela fornece endpoints para 
criação de processos, upload e download de arquivos, listagem de processos, 
consulta por ID ou código e acesso aos arquivos relacionados a cada processo.
*/

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
            // Monta DTO com os dados do formulário
            ProcessoRequestDTO dto = new ProcessoRequestDTO();
            dto.setTitulo(titulo);
            dto.setTipo(tipo);
            dto.setModal(modal);
            dto.setObservacao(observacao);
            dto.setArquivos(arquivos);

            // Chama serviço para criar processo
            Processo created = service.criarProcesso(dto);

            // Retorna o processo criado com status 201
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
            // Salva os arquivos anexados ao processo
            service.salvarArquivosNoProcesso(codigo, arquivos);

            // Retorna sucesso com quantidade de arquivos enviados
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
        // Retorna lista de todos os processos com quantidade de arquivos
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
                    // Converte entidade para DTO e retorna
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
            // Busca lista de arquivos como DTOs (sem o conteúdo binário)
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
            // Busca arquivo pelo ID
            ProcessoArquivo arq = arquivoRepo.findById(idArquivo)
                    .orElseThrow(() -> new RuntimeException("Arquivo não encontrado"));

            // Retorna arquivo como attachment
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
            // Busca todos os arquivos do banco
            List<ProcessoArquivo> arquivos = arquivoRepo.findAll();

            // Converte para DTOs resumidos
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
    //   BUSCAR PROCESSO POR CÓDIGO
    // ================================
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProcessoResponseDTO> getProcessoPorCodigo(@PathVariable String codigo) {
        return repository.findByCodigo(codigo)
                .map(p -> {
                    // Converte entidade para DTO e retorna
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
