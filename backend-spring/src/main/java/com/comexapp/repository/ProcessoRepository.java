package com.comexapp.repository;

import com.comexapp.model.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProcessoRepository extends JpaRepository<Processo, Long> {
    Processo findByCodigo(String codigo);
}
