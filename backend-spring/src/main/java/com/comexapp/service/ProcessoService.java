package com.comexapp.service;

import com.comexapp.DTO.ProcessoRequestDTO;
import com.comexapp.model.Processo;
import com.comexapp.repository.ProcessoRepository;
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

    String prefix = (dto.getTipo().equalsIgnoreCase("importacao")
            ? (dto.getModal().equalsIgnoreCase("maritimo") ? "INM" : "INA")
            : (dto.getModal().equalsIgnoreCase("maritimo") ? "EXM" : "EXA"));

    String codigo;
    do {
        codigo = prefix + "_" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    } while (repo.existsByCodigo(codigo));

    p.setCodigo(codigo);
    p.setTitulo(dto.getTitulo());
    p.setTipo(dto.getTipo());
    p.setModal(dto.getModal());
    p.setObservacao(dto.getObservacao());

    // === 🔥 NOVO: junta todos arquivos em um array único (opcional) ===
    if (dto.getArquivos() != null && dto.getArquivos().length > 0) {
        try {
            // se quiser armazenar 1 único arquivo por processo: usa o 1º
            MultipartFile file = dto.getArquivos()[0];
            p.setArquivos(file.getBytes());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter arquivo", e);
        }
    }

    return repo.save(p);
}

}
