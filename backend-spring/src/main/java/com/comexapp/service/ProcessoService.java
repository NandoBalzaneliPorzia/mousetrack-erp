package com.comexapp.service;

import com.comexapp.DTO.ProcessoRequestDTO;
import com.comexapp.model.Processo;
import com.comexapp.model.ArquivoProcesso;
import com.comexapp.repository.ProcessoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ProcessoService {

    private final ProcessoRepository repo;

    public ProcessoService(ProcessoRepository repo) {
        this.repo = repo;
    }

    public Processo criarProcesso(ProcessoRequestDTO dto) {

        Processo p = new Processo();
        p.setTitulo(dto.getTitulo());
        p.setTipo(dto.getTipo());
        p.setModal(dto.getModal());
        p.setObservacao(dto.getObservacao());

        // ===============================================================
        // 🔥 1. Gera prefixo do código (INA, INM, EXA, EXM)
        // ===============================================================
        String prefix = (dto.getTipo().equalsIgnoreCase("importacao")
                ? (dto.getModal().equalsIgnoreCase("maritimo") ? "INM" : "INA")
                : (dto.getModal().equalsIgnoreCase("maritimo") ? "EXM" : "EXA"));

        // ===============================================================
        // 🔥 2. Lógica de retry — tenta gerar código único até 5x
        // ===============================================================
        for (int attempt = 0; attempt < 5; attempt++) {

            String codigo = prefix + "_" +
                    UUID.randomUUID().toString().substring(0, 4).toUpperCase();

            p.setCodigo(codigo);

            try {
                // Agora corretamente processa TODOS os arquivos
                processarArquivos(dto, p);

                // Salva o processo com seus arquivos
                return repo.save(p);

            } catch (DataIntegrityViolationException ex) {

                if (attempt == 4) {
                    throw new RuntimeException("Erro ao gerar código único para o processo.", ex);
                }
            }
        }

        throw new RuntimeException("Falha inesperada ao criar processo.");
    }

    // ===============================================================
    // 🔥 3. Método auxiliar — lê e salva TODOS os arquivos
    // ===============================================================
    private void processarArquivos(ProcessoRequestDTO dto, Processo p) {
        if (dto.getArquivos() == null) return;

        for (MultipartFile file : dto.getArquivos()) {
            if (file.isEmpty()) continue;

            try {
                ArquivoProcesso arq = new ArquivoProcesso();
                arq.setNomeArquivo(file.getOriginalFilename());
                arq.setTipoArquivo(file.getContentType());
                arq.setDados(file.getBytes());
                arq.setProcesso(p);

                p.getArquivos().add(arq);

            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar arquivos.", e);
            }
        }
    }
}
