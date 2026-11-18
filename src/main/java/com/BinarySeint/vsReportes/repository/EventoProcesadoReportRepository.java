package com.BinarySeint.vsReportes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.BinarySeint.vsReportes.classes.EventoProcesadoReport;

@Repository
public interface EventoProcesadoReportRepository extends JpaRepository<EventoProcesadoReport, String> {
}