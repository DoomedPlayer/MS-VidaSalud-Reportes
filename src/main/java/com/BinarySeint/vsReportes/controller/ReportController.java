package com.BinarySeint.vsReportes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.BinarySeint.vsReportes.classes.KpiAtenciones;
import com.BinarySeint.vsReportes.classes.PrestacionDemanda;
import com.BinarySeint.vsReportes.repository.KpiAtencionesRepository;
import com.BinarySeint.vsReportes.repository.PrestacionDemandaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final KpiAtencionesRepository kpiRepository;
    private final PrestacionDemandaRepository demandaRepository;

    public ReportController(KpiAtencionesRepository kpiRepository, PrestacionDemandaRepository demandaRepository) {
        this.kpiRepository = kpiRepository;
        this.demandaRepository = demandaRepository;
    }

    @GetMapping("/kpis/today")
    public ResponseEntity<KpiAtenciones> getKpisToday() {
        Optional<KpiAtenciones> kpi = kpiRepository.findByFecha(LocalDate.now());
        return kpi.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/kpis")
    public ResponseEntity<List<KpiAtenciones>> getAllKpis(
            @RequestParam(required = false, defaultValue = "last24h") String range) {
        if ("last24h".equalsIgnoreCase(range) || "today".equalsIgnoreCase(range)) {
            return kpiRepository.findByFecha(LocalDate.now())
                    .map(kpi -> ResponseEntity.ok(List.of(kpi)))
                    .orElseGet(() -> ResponseEntity.ok(List.of()));
        }
        return ResponseEntity.ok(kpiRepository.findAll());
    }

    @GetMapping("/top-services")
    public ResponseEntity<List<PrestacionDemanda>> getTopServices(
            @RequestParam(required = false, defaultValue = "last7d") String range) {
        List<PrestacionDemanda> topPrestaciones = demandaRepository.findTop5ByOrderByCantidadSolicitudesDesc();
        return ResponseEntity.ok(topPrestaciones);
    }
}