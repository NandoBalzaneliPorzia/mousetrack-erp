package com.comexapp.service;

/*
A classe ProcessoService.java gerencia a criação e manipulação de processos no sistema.
Fornece funcionalidades para criar novos processos com arquivos anexados, salvar arquivos 
no sistema de arquivos local e no banco de dados, e adicionar novos arquivos a processos 
existentes.
*/

import com.comexapp.DTO.ProcessoRequestDTO;
import com.comexapp.model.Processo;
import com.comexapp.model.ProcessoArquivo;
import com.comexapp.repository.ProcessoRepository;
import com.comexapp.repository.ProcessoArquivoRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessoService {

    private final ProcessoRepository repo;
    private final ProcessoArquivoRepository arquivoRepo;

    private static final String ROOT_DIR = "uploads";

    public ProcessoService(ProcessoRepository repo, ProcessoArquivoRepository arquivoRepo) {
        this.repo = repo;
        this.arquivoRepo = arquivoRepo;
    }

    // ============================================================
    //  CRIAR PROCESSO + ARQUIVOS DO FORMULÁRIO
    // ============================================================
    public Processo criarProcesso(ProcessoRequestDTO dto) throws Exception {

        Processo processo = new Processo();
        processo.setTitulo(dto.getTitulo());
        processo.setTipo(dto.getTipo());
        processo.setModal(dto.getModal());
        processo.setObservacao(dto.getObservacao());
        processo.setCodigo(UUID.randomUUID().toString().substring(0, 8));
        processo.setArquivos(new ArrayList<>());
        processo.setDataCriacao(LocalDateTime.now().toString());
        processo.setResponsavel("Sistema");

        processo = repo.save(processo);

        // Criar pastas
        Path rootPath = Paths.get(ROOT_DIR);
        Files.createDirectories(rootPath);

        Path pastaProcesso = rootPath.resolve(processo.getCodigo());
        Files.createDirectories(pastaProcesso);

        // Salvar arquivos iniciais
        if (dto.getArquivos() != null) {
            for (MultipartFile file : dto.getArquivos()) {
                if (file != null && !file.isEmpty()) {

                    ProcessoArquivo pa = new ProcessoArquivo();
                    pa.setNomeArquivo(file.getOriginalFilename());
                    pa.setTipoArquivo(file.getContentType());
                    pa.setDadosArquivo(file.getBytes());
                    pa.setDataCriacao(LocalDateTime.now().toString());
                    pa.setProcesso(processo);

                    arquivoRepo.save(pa);
                    processo.getArquivos().add(pa);

                    Files.write(pastaProcesso.resolve(file.getOriginalFilename()), file.getBytes());
                }
            }
        }

        return repo.save(processo);
    }

    // ============================================================
    //  ANEXAR ARQUIVOS APÓS O CARD EXISTIR
    // ============================================================
    public Processo salvarArquivosNoProcesso(String codigo, MultipartFile[] arquivos) throws Exception {

        Processo processo = repo.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Processo não encontrado: " + codigo));

        Path pastaProcesso = Paths.get(ROOT_DIR).resolve(codigo);
        Files.createDirectories(pastaProcesso);

        for (MultipartFile file : arquivos) {
            if (file == null || file.isEmpty()) continue;

            ProcessoArquivo pa = new ProcessoArquivo();
            pa.setNomeArquivo(file.getOriginalFilename());
            pa.setTipoArquivo(file.getContentType());
            pa.setDadosArquivo(file.getBytes());
            pa.setDataCriacao(LocalDateTime.now().toString());
            pa.setProcesso(processo);

            arquivoRepo.save(pa);
            processo.getArquivos().add(pa);

            Files.write(pastaProcesso.resolve(file.getOriginalFilename()), file.getBytes());
        }

        return repo.save(processo);
    }
}
