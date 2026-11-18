package com.BinarySeint.vsReportes.classes;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "reporte_tiempos_espera")
public class TiempoEsperaRegistro {

    @Id
    private Long appointmentId;
    private LocalDateTime inicioEspera;
    private LocalDateTime inicioAtencion;
    private Double minutosEspera;

    public TiempoEsperaRegistro() {}

    public TiempoEsperaRegistro(Long appointmentId, LocalDateTime inicioEspera) {
        this.appointmentId = appointmentId;
        this.inicioEspera = inicioEspera;
    }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public LocalDateTime getInicioEspera() { return inicioEspera; }
    public void setInicioEspera(LocalDateTime inicioEspera) { this.inicioEspera = inicioEspera; }

    public LocalDateTime getInicioAtencion() { return inicioAtencion; }
    public void setInicioAtencion(LocalDateTime inicioAtencion) { this.inicioAtencion = inicioAtencion; }

    public Double getMinutosEspera() { return minutosEspera; }
    public void setMinutosEspera(Double minutosEspera) { this.minutosEspera = minutosEspera; }
}