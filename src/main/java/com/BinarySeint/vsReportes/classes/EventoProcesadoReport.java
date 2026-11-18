package com.BinarySeint.vsReportes.classes;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "reporte_eventos_procesados")
public class EventoProcesadoReport {

    @Id
    private String eventId;
    private String topico;
    private LocalDateTime fechaProcesamiento;

    public EventoProcesadoReport() {}

    public EventoProcesadoReport(String eventId, String topico, LocalDateTime fechaProcesamiento) {
        this.eventId = eventId;
        this.topico = topico;
        this.fechaProcesamiento = fechaProcesamiento;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getTopico() { return topico; }
    public void setTopico(String topico) { this.topico = topico; }

    public LocalDateTime getFechaProcesamiento() { return fechaProcesamiento; }
    public void setFechaProcesamiento(LocalDateTime fechaProcesamiento) { this.fechaProcesamiento = fechaProcesamiento; }
}