package com.comexapp.service;

import com.comexapp.DTO.ProcessoRequestDTO;
import com.comexapp.model.Processo;
import com.comexapp.model.ProcessoArquivo;
import com.comexapp.repository.ProcessoRepository;
import com.comexapp.repository.ProcessoArquivoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class ProcessoService {

    private final ProcessoRepository repo;
    private final ProcessoArquivoRepository arquivoRepo;

    public ProcessoService(ProcessoRepository repo, ProcessoArquivoRepository arquivoRepo) {
        this.repo = repo;
        this.arquivoRepo = arquivoRepo;
    }

    public Processo criarProcesso(ProcessoRequestDTO dto) throws Exception {
        Processo processo = new Processo();
        processo.setTitulo(dto.getTitulo());
        processo.setTipo(dto.getTipo());
        processo.setModal(dto.getModal());
        processo.setObservacao(dto.getObservacao());
        processo.setCodigo(UUID.randomUUID().toString().substring(0, 8));

        processo = repo.save(processo);

        if (dto.getArquivos() != null) {
            for (MultipartFile file : dto.getArquivos()) {
                if (!file.isEmpty()) {
                    ProcessoArquivo pa = new ProcessoArquivo();
                    pa.setNomeArquivo(file.getOriginalFilename());
                    pa.setTipoArquivo(file.getContentType());
                    pa.setDadosArquivo(file.getBytes());
                    pa.setProcesso(processo);

                    arquivoRepo.save(pa);
                    processo.getArquivos().add(pa);
                }
            }
        }

        return repo.save(processo);
    }
}
