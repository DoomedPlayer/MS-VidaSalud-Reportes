package com.BinarySeint.vsReportes.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.BinarySeint.vsReportes.DTO.AtencionEventDto;
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

   @PostMapping("/event")
    public ResponseEntity<Void> procesarEventoAtencionREST(@RequestBody AtencionEventDto evento) {
        LocalDate hoy = LocalDate.now();
        KpiAtenciones kpi = kpiRepository.findByFecha(hoy).orElse(new KpiAtenciones());
        kpi.setFecha(hoy);

        switch (evento.getEstado()) {
            case "SOLICITADA":
                kpi.setAtencionesSolicitadas(kpi.getAtencionesSolicitadas() + 1);

                if (evento.getPrestacionId() != null) {
                    PrestacionDemanda demanda = demandaRepository.findById(evento.getPrestacionId())
                            .orElse(new PrestacionDemanda(evento.getPrestacionId(), 0L));
                    demanda.setCantidadSolicitudes(demanda.getCantidadSolicitudes() + 1);
                    demandaRepository.save(demanda);
                }
                break;
            case "CONFIRMADA":
                kpi.setAtencionesConfirmadas(kpi.getAtencionesConfirmadas() + 1);
                break;
            case "CERRADA":
                kpi.setAtencionesCerradas(kpi.getAtencionesCerradas() + 1);
                break;
        }

        kpiRepository.save(kpi);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/kpis/today")
    public ResponseEntity<KpiAtenciones> getKpisToday() {
        Optional<KpiAtenciones> kpi = kpiRepository.findByFecha(LocalDate.now());
        return kpi.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/kpis")
    public ResponseEntity<List<KpiAtenciones>> getAllKpis() {
        return ResponseEntity.ok(kpiRepository.findAll());
    }

    // ENDPOINT ACTUALIZADO
    @GetMapping("/top-services")
    public ResponseEntity<List<PrestacionDemanda>> getTopServices(@RequestParam(required = false, defaultValue = "last7d") String range) {
        List<PrestacionDemanda> topPrestaciones = demandaRepository.findTop5ByOrderByCantidadSolicitudesDesc();
        return ResponseEntity.ok(topPrestaciones);
    }
}