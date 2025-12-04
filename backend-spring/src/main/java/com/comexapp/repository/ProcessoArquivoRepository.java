package com.comexapp.repository;

/*
A interface ProcessoArquivoRepository é um repositório Spring Data JPA para a entidade ProcessoArquivo.
Ela fornece métodos para operações CRUD e consultas específicas, como:
- Buscar arquivos de um processo pelo código (em DTO para não trazer dados binários)
- Contar a quantidade de arquivos de um processo
*/

import com.comexapp.model.ProcessoArquivo;
import com.comexapp.DTO.ProcessoArquivoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProcessoArquivoRepository extends JpaRepository<ProcessoArquivo, Long> {

    // Retorna apenas DTOs sem o campo LOB, filtrando pelo código do processo
    @Query("SELECT new com.comexapp.DTO.ProcessoArquivoDTO(a.id, a.nomeArquivo, a.tipoArquivo, a.dataCriacao, a.processo.codigo) " +
           "FROM ProcessoArquivo a WHERE a.processo.codigo = :codigo")
    List<ProcessoArquivoDTO> findDTOByProcessoCodigo(@Param("codigo") String codigo);

    // Retorna a lista completa de arquivos de um processo
    List<ProcessoArquivo> findByProcesso_Codigo(String codigo);

    // Conta a quantidade de arquivos de um processo pelo id do processo
    @Query("SELECT COUNT(a) FROM ProcessoArquivo a WHERE a.processo.id = :processoId")
    int countByProcessoId(@Param("processoId") Long processoId);
}
