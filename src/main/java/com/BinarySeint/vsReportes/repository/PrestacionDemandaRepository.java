package com.BinarySeint.vsReportes.repository;

import com.BinarySeint.vsReportes.classes.PrestacionDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestacionDemandaRepository extends JpaRepository<PrestacionDemanda, Long> {
    List<PrestacionDemanda> findTop5ByOrderByCantidadSolicitudesDesc();
}