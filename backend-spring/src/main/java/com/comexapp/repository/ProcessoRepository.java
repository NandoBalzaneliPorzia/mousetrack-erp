package com.comexapp.repository;

/*
A classe ProcessoRepository.java é um repositório que gerencia a entidade
Processo no banco de dados, fornecendo operações de persistência e consulta 
de processos. Inclui funcionalidade para buscar um processo específico pelo 
seu código único.
*/

import com.comexapp.model.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

    public interface ProcessoRepository extends JpaRepository<Processo, Long> {
        Optional<Processo> findByCodigo(String codigo);
}