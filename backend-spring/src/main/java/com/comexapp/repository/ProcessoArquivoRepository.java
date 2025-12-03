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
import java.util.List;

public interface ProcessoArquivoRepository extends JpaRepository<ProcessoArquivo, Long> {

    List<ProcessoArquivo> findByProcessoCodigo(String codigo);

}
