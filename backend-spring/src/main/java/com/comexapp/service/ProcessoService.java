package com.comexapp.service;

import com.comexapp.DTO.ProcessoRequestDTO;
import com.comexapp.model.Processo;
import com.comexapp.repository.ProcessoRepository;
import com.comexapp.util.FileStorageUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProcessoService {

    private final ProcessoRepository repo;
    private final FileStorageUtil storage;

    public ProcessoService(ProcessoRepository repo, FileStorageUtil storage) {
        this.repo = repo;
        this.storage = storage;
    }

    public Processo criarProcesso(ProcessoRequestDTO dto) {
        Processo p = new Processo();

        // prefixo: INA / INM / EXA / EXM
        String prefix = (dto.getTipo().equals("importacao")
                ? (dto.getModal().equals("maritimo") ? "INM" : "INA")
                : (dto.getModal().equals("maritimo") ? "EXM" : "EXA"));

        // gera código curto único (coloca loop pra garantir unicidade)
        String codigo;
        do {
            codigo = prefix + "_" + UUID.randomUUID().toString().substring(0,4).toUpperCase();
        } while (repo.existsByCodigo(codigo));

        p.setCodigo(codigo);
        p.setTitulo(dto.getTitulo());
        p.setTipo(dto.getTipo());
        p.setModal(dto.getModal());
        p.setObservacao(dto.getObservacao());

        if (dto.getArquivos() != null && dto.getArquivos().length > 0) {
            String paths = storage.saveFiles(dto.getArquivos());
            p.setArquivos(paths);
        }

        return repo.save(p);
    }
}
