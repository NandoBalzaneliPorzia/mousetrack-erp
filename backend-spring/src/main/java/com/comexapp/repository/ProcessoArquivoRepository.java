package com.comexapp.repository;

import com.comexapp.model.ProcessoArquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProcessoArquivoRepository extends JpaRepository<ProcessoArquivo, Long> {

    List<ProcessoArquivo> findByProcessoCodigo(String codigo);

}
