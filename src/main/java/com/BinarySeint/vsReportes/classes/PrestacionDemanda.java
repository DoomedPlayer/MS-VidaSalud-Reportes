package com.BinarySeint.vsReportes.classes;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PrestacionDemanda {

    @Id
    private Long prestacionId;
    private Long cantidadSolicitudes;

    public PrestacionDemanda() {}

    public PrestacionDemanda(Long prestacionId, Long cantidadSolicitudes) {
        this.prestacionId = prestacionId;
        this.cantidadSolicitudes = cantidadSolicitudes;
    }

    public Long getPrestacionId() { return prestacionId; }
    public void setPrestacionId(Long prestacionId) { this.prestacionId = prestacionId; }
    public Long getCantidadSolicitudes() { return cantidadSolicitudes; }
    public void setCantidadSolicitudes(Long cantidadSolicitudes) { this.cantidadSolicitudes = cantidadSolicitudes; }
}