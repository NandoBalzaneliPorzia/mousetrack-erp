package com.comexapp.controller;

import com.comexapp.DTO.ProcessoRequestDTO;
import com.comexapp.model.Processo;
import com.comexapp.repository.ProcessoRepository;
import com.comexapp.model.ProcessoArquivo;
import com.comexapp.repository.ProcessoArquivoRepository;
import com.comexapp.service.ProcessoService;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
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
    //      CRIAR PROCESSO + ARQUIVOS
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

            // *** CHAMA O SERVICE QUE SALVA PROCESSO E ARQUIVOS ***
            Processo created = service.criarProcesso(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erro ao criar processo", "detail", e.getMessage()));
        }
    }

    @PostMapping(value = "/{codigo}/arquivos", consumes = "multipart/form-data")
public ResponseEntity<?> uploadArquivos(
        @PathVariable String codigo,
        @RequestPart("arquivos") MultipartFile[] arquivos
) {
    try {
        Processo processo = service.salvarArquivosNoProcesso(codigo, arquivos);
        // 🔥 NÃO retornar o processo inteiro (gera recursão + envia byte[] gigante)
        return ResponseEntity.ok(Map.of(
                "message", "Arquivos enviados",
                "count", arquivos.length));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Falha ao anexar arquivos", "detail", e.getMessage()));
    }
}

    // ================================
    //      LISTAR PROCESSOS
    // ================================
    @GetMapping
    public List<Processo> listarProcessos() {
        return repository.findAll();
    }

    // ================================
    //      BUSCAR PROCESSO POR ID
    // ================================
    @GetMapping("/{id}")
    public ResponseEntity<Processo> getProcesso(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

// ================================ LISTAR ARQUIVOS DE UM PROCESSO ================================
    @GetMapping("/{codigo}/arquivos")
public ResponseEntity<?> listarArquivos(@PathVariable String codigo) {
    try {
        Processo processo = repository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Processo não encontrado"));

        return ResponseEntity.ok(
                processo.getArquivos().stream().map(a -> Map.of(
    "id", a.getId(),
    "nome", a.getNomeArquivo(),
    "tipo", a.getTipoArquivo(),
    "dataCriacao", a.getDataCriacao() != null ? a.getDataCriacao().toString() : ""
)));

    } catch (Exception e) {
        return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
    }
}
// ================================ DOWNLOAD DE ARQUIVO ================================
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

}
