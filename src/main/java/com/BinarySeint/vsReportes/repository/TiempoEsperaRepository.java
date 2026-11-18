package com.BinarySeint.vsReportes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.BinarySeint.vsReportes.classes.TiempoEsperaRegistro;

@Repository
public interface TiempoEsperaRepository extends JpaRepository<TiempoEsperaRegistro, Long> {
}