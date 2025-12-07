package com.comexapp.repository;

/*
Responsável: Laura Pereira
A interface ProcessoRepository é um repositório Spring Data JPA para a entidade Processo.
Ela fornece operações padrão de CRUD e métodos customizados, incluindo:
- Busca de um processo pelo código único
- Busca de todos os processos trazendo também os arquivos associados
*/

import com.comexapp.model.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ProcessoRepository extends JpaRepository<Processo, Long> {

    // Busca um processo pelo código único
    Optional<Processo> findByCodigo(String codigo);

    // Busca todos os processos e carrega também os arquivos associados (fetch join)
    @Query("SELECT DISTINCT p FROM Processo p LEFT JOIN FETCH p.arquivos")
    List<Processo> findAllWithArquivos();
}
