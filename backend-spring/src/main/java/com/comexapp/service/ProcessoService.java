package com.comexapp.service;

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
import java.util.UUID;
import java.util.ArrayList;

@Service
public class ProcessoService {

    private final ProcessoRepository repo;
    private final ProcessoArquivoRepository arquivoRepo;

    // Pasta raiz onde todos os processos serão salvos
    private static final String ROOT_DIR = "uploads";

    public ProcessoService(ProcessoRepository repo, ProcessoArquivoRepository arquivoRepo) {
        this.repo = repo;
        this.arquivoRepo = arquivoRepo;
    }

    // ============================================================
    //  MÉTODO EXISTENTE — CRIAR PROCESSO
    // ============================================================
    public Processo criarProcesso(ProcessoRequestDTO dto) throws Exception {

        Processo processo = new Processo();
        processo.setTitulo(dto.getTitulo());
        processo.setTipo(dto.getTipo());
        processo.setModal(dto.getModal());
        processo.setObservacao(dto.getObservacao());
        processo.setCodigo(UUID.randomUUID().toString().substring(0, 8));

        processo.setArquivos(new ArrayList<>());

        processo = repo.save(processo);

        Path rootPath = Paths.get(ROOT_DIR);
        Files.createDirectories(rootPath);

        Path pastaProcesso = rootPath.resolve(processo.getCodigo());
        Files.createDirectories(pastaProcesso);

        if (dto.getArquivos() != null) {
            for (MultipartFile file : dto.getArquivos()) {
                if (file != null && !file.isEmpty()) {

                    ProcessoArquivo pa = new ProcessoArquivo();
                    pa.setNomeArquivo(file.getOriginalFilename());
                    pa.setTipoArquivo(file.getContentType());
                    pa.setDadosArquivo(file.getBytes());
                    pa.setProcesso(processo);

                    arquivoRepo.save(pa);
                    processo.getArquivos().add(pa);

                    Path destino = pastaProcesso.resolve(file.getOriginalFilename());
                    Files.write(destino, file.getBytes());
                }
            }
        }

        return repo.save(processo);
    }

    // ============================================================
    //  NOVO MÉTODO — ADICIONAR ARQUIVOS DEPOIS QUE O PROCESSO EXISTE
    // ============================================================
    public Processo salvarArquivosNoProcesso(String codigo, MultipartFile[] arquivos) throws Exception {

        // Buscar processo pelo código
        Processo processo = repo.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Processo não encontrado: " + codigo));

        // Criar pasta do processo se ainda não existir
        Path pastaProcesso = Paths.get(ROOT_DIR).resolve(codigo);
        Files.createDirectories(pastaProcesso);

        // Processar cada arquivo
        for (MultipartFile file : arquivos) {

            if (file == null || file.isEmpty()) continue;

            // Salvar no banco
            ProcessoArquivo pa = new ProcessoArquivo();
            pa.setNomeArquivo(file.getOriginalFilename());
            pa.setTipoArquivo(file.getContentType());
            pa.setDadosArquivo(file.getBytes());
            pa.setProcesso(processo);

            arquivoRepo.save(pa);
            processo.getArquivos().add(pa);

            // Salvar fisicamente
            Path destino = pastaProcesso.resolve(file.getOriginalFilename());
            Files.write(destino, file.getBytes());
        }

        // Retornar processo atualizado com os novos anexos
        return repo.save(processo);
    }
}
