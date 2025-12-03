package com.comexapp.repository;

/*
A classe ProcessoArquivoRepository.java é um repositório que gerencia a 
entidade ProcessoArquivo no banco de dados, fornecendo operações de 
persistência e consulta de arquivos associados a processos. Inclui 
funcionalidade para buscar todos os arquivos vinculados a um processo 
específico pelo seu código.
*/

import com.comexapp.model.ProcessoArquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProcessoArquivoRepository extends JpaRepository<ProcessoArquivo, Long> {

    List<ProcessoArquivo> findByProcessoCodigo(String codigo);

    // Método para contar arquivos por processo (usando o id do processo)
    @Query("SELECT COUNT(a) FROM ProcessoArquivo a WHERE a.processo.id = :processoId")
    int countByProcessoId(@Param("processoId") Long processoId);
}
