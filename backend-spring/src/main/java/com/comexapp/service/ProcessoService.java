package com.comexapp.service;

import com.comexapp.DTO.ProcessoRequestDTO;
import com.comexapp.model.Processo;
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
                // Tenta salvar o processo
                // se o código existir, cairá no catch
                processarArquivos(dto, p);
                return repo.save(p);

            } catch (DataIntegrityViolationException ex) {
                // conflito de código gerado — tenta de novo
                if (attempt == 4) {
                    throw new RuntimeException("Erro ao gerar código único para o processo.", ex);
                }
            }
        }

        throw new RuntimeException("Falha inesperada ao criar processo.");
    }

    // ===============================================================
    // 🔥 3. Método auxiliar — lê arquivos do DTO
    // ===============================================================
    private void processarArquivos(ProcessoRequestDTO dto, Processo p) {
        if (dto.getArquivos() != null && dto.getArquivos().length > 0) {
            try {
                // Aqui estamos armazenando APENAS o primeiro arquivo
                MultipartFile file = dto.getArquivos()[0];
                p.setArquivos(file.getBytes());

            } catch (Exception e) {
                throw new RuntimeException("Erro ao processar arquivos.", e);
            }
        }
    }
}
