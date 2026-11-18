package com.BinarySeint.vsReportes.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BinarySeint.vsReportes.DTO.AuditTimelineEventDTO;
import com.BinarySeint.vsReportes.DTO.CitasEventoDTO;
import com.BinarySeint.vsReportes.classes.EventoProcesadoReport;
import com.BinarySeint.vsReportes.classes.KpiAtenciones;
import com.BinarySeint.vsReportes.classes.PrestacionDemanda;
import com.BinarySeint.vsReportes.classes.TiempoEsperaRegistro;
import com.BinarySeint.vsReportes.repository.EventoProcesadoReportRepository;
import com.BinarySeint.vsReportes.repository.KpiAtencionesRepository;
import com.BinarySeint.vsReportes.repository.PrestacionDemandaRepository;
import com.BinarySeint.vsReportes.repository.TiempoEsperaRepository;

@Service
public class ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteService.class);

    private final KpiAtencionesRepository kpiRepository;
    private final PrestacionDemandaRepository demandaRepository;
    private final TiempoEsperaRepository tiempoEsperaRepository;
    private final EventoProcesadoReportRepository eventoProcesadoRepository;

    public ReporteService(KpiAtencionesRepository kpiRepository,
                          PrestacionDemandaRepository demandaRepository,
                          TiempoEsperaRepository tiempoEsperaRepository,
                          EventoProcesadoReportRepository eventoProcesadoRepository) {
        this.kpiRepository = kpiRepository;
        this.demandaRepository = demandaRepository;
        this.tiempoEsperaRepository = tiempoEsperaRepository;
        this.eventoProcesadoRepository = eventoProcesadoRepository;
    }

    @Transactional
    public void procesarEventoCita(CitasEventoDTO evento) {
        if (evento == null || evento.getAppointmentId() == null || evento.getEstadoActual() == null) {
            throw new IllegalArgumentException("Payload inválido en appointments.events: falta appointmentId o estadoActual.");
        }

        if (evento.getEventId() != null && eventoProcesadoRepository.existsById(evento.getEventId())) {
            log.warn("⚠️ Evento duplicado ignorado en reportes: {}", evento.getEventId());
            return;
        }

        LocalDateTime fechaEvento = evento.getTimestamp() != null ? evento.getTimestamp() : LocalDateTime.now();
        LocalDate dia = fechaEvento.toLocalDate();

        KpiAtenciones kpi = kpiRepository.findByFecha(dia).orElseGet(() -> {
            KpiAtenciones nuevo = new KpiAtenciones();
            nuevo.setFecha(dia);
            return nuevo;
        });

        String estadoActual = evento.getEstadoActual().toUpperCase();
        String estadoAnterior = evento.getEstadoAnterior() != null ? evento.getEstadoAnterior().toUpperCase() : "";

        switch (estadoActual) {
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

            case "EN_ESPERA":
                kpi.setAtencionesEnEspera(kpi.getAtencionesEnEspera() + 1);
                tiempoEsperaRepository.save(new TiempoEsperaRegistro(evento.getAppointmentId(), fechaEvento));
                break;

            case "EN_ATENCION":
                if ("EN_ESPERA".equals(estadoAnterior) && kpi.getAtencionesEnEspera() > 0) {
                    kpi.setAtencionesEnEspera(kpi.getAtencionesEnEspera() - 1);
                }
                actualizarTiempoEspera(kpi, evento.getAppointmentId(), fechaEvento);
                break;

            case "CERRADA":
                kpi.setAtencionesCerradas(kpi.getAtencionesCerradas() + 1);
                break;

            case "CANCELADA":
                kpi.setAtencionesCanceladas(kpi.getAtencionesCanceladas() + 1);
                if ("EN_ESPERA".equals(estadoAnterior) && kpi.getAtencionesEnEspera() > 0) {
                    kpi.setAtencionesEnEspera(kpi.getAtencionesEnEspera() - 1);
                }
                break;

            default:
                log.warn("⚠️ Estado no reconocido para KPI: {}", estadoActual);
        }

        kpiRepository.save(kpi);

        if (evento.getEventId() != null) {
            eventoProcesadoRepository.save(new EventoProcesadoReport(evento.getEventId(), "appointments.events", LocalDateTime.now()));
        }

        log.info("📊 KPIs actualizados para fecha {} | Cita ID: {} | Estado: {}",
                dia, evento.getAppointmentId(), estadoActual);
    }

    @Transactional
    public void procesarEventoOperativoAudit(AuditTimelineEventDTO evento) {
        if (evento == null || evento.getAccion() == null) {
            throw new IllegalArgumentException("Payload inválido en audit.timeline: falta acción.");
        }

        if (evento.getEventId() != null && eventoProcesadoRepository.existsById("AUDIT-" + evento.getEventId())) {
            return;
        }

        if (evento.getEventId() != null) {
            eventoProcesadoRepository.save(new EventoProcesadoReport("AUDIT-" + evento.getEventId(), "audit.timeline", LocalDateTime.now()));
        }

        log.info("📈 Traza operativa registrada en analítica [Usuario: {} | Acción: {} | Cita ID: {}]",
                evento.getUsuarioId(), evento.getAccion(), evento.getAppointmentId());
    }

    private void actualizarTiempoEspera(KpiAtenciones kpi, Long appointmentId, LocalDateTime finEspera) {
        tiempoEsperaRepository.findById(appointmentId).ifPresent(registro -> {
            if (registro.getInicioEspera() != null && registro.getMinutosEspera() == null) {
                double minutos = Math.max(0.0, Duration.between(registro.getInicioEspera(), finEspera).toSeconds() / 60.0);
                registro.setInicioAtencion(finEspera);
                registro.setMinutosEspera(Math.round(minutos * 100.0) / 100.0);
                tiempoEsperaRepository.save(registro);

                int n = kpi.getCantidadMedicionesEspera() != null ? kpi.getCantidadMedicionesEspera() : 0;
                double promedioActual = kpi.getTiempoPromedioEsperaMinutos() != null ? kpi.getTiempoPromedioEsperaMinutos() : 0.0;
                double nuevoPromedio = ((promedioActual * n) + minutos) / (n + 1);

                kpi.setCantidadMedicionesEspera(n + 1);
                kpi.setTiempoPromedioEsperaMinutos(Math.round(nuevoPromedio * 100.0) / 100.0);
            }
        });
    }
}