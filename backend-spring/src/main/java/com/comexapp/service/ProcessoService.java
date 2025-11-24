package com.comexapp.service;

import com.comexapp.DTO.ProcessoRequestDTO;
import com.comexapp.model.Processo;
import com.comexapp.model.ProcessoArquivo;
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

    public Processo criarProcesso(ProcessoRequestDTO dto) throws Exception {

        // Criar processo básico
        Processo processo = new Processo();
        processo.setTitulo(dto.getTitulo());
        processo.setTipo(dto.getTipo());
        processo.setModal(dto.getModal());
        processo.setObservacao(dto.getObservacao());

        // Código único
        processo.setCodigo(UUID.randomUUID().toString().substring(0, 8));

        // Salva processo primeiro para gerar ID
        processo = repo.save(processo);

        // Salvar arquivos
        if (dto.getArquivos() != null) {
            for (MultipartFile file : dto.getArquivos()) {

                if (!file.isEmpty()) {
                    ProcessoArquivo pa = new ProcessoArquivo();
                    pa.setNomeArquivo(file.getOriginalFilename());
                    pa.setTipoArquivo(file.getContentType());
                    pa.setDadosArquivo(file.getBytes());
                    pa.setProcesso(processo);

                    processo.getArquivos().add(pa);
                }
            }
        }

        // Persistir arquivos e processo
        return repo.save(processo);
    }
}
