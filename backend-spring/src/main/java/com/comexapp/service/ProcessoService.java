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

    public Processo criarProcesso(ProcessoRequestDTO dto) throws Exception {

        // ================================
        // 1. Criar objeto Processo
        // ================================
        Processo processo = new Processo();
        processo.setTitulo(dto.getTitulo());
        processo.setTipo(dto.getTipo());
        processo.setModal(dto.getModal());
        processo.setObservacao(dto.getObservacao());
        processo.setCodigo(UUID.randomUUID().toString().substring(0, 8));

        // Evita NullPointerException
        processo.setArquivos(new ArrayList<>());

        // ================================
        // 2. Salvar processo no banco (gera ID)
        // ================================
        processo = repo.save(processo);

        // ================================
        // 3. Criar pasta física do processo
        // ================================
        Path rootPath = Paths.get(ROOT_DIR);
        Files.createDirectories(rootPath); // garante que uploads/ existe

        Path pastaProcesso = rootPath.resolve(processo.getCodigo());
        Files.createDirectories(pastaProcesso);

        // ================================
        // 4. Salvar cada arquivo
        // ================================
        if (dto.getArquivos() != null) {
            for (MultipartFile file : dto.getArquivos()) {

                if (file != null && !file.isEmpty()) {

                    // ------------------------------------
                    // 4.1 Salvar no banco
                    // ------------------------------------
                    ProcessoArquivo pa = new ProcessoArquivo();
                    pa.setNomeArquivo(file.getOriginalFilename());
                    pa.setTipoArquivo(file.getContentType());
                    pa.setDadosArquivo(file.getBytes());
                    pa.setProcesso(processo);

                    arquivoRepo.save(pa);
                    processo.getArquivos().add(pa);

                    // ------------------------------------
                    // 4.2 Salvar arquivo fisicamente
                    // ------------------------------------
                    Path destino = pastaProcesso.resolve(file.getOriginalFilename());
                    Files.write(destino, file.getBytes());
                }
            }
        }

        // ================================
        // 5. Retornar processo atualizado
        // ================================
        return repo.save(processo);
    }
}
