package com.BinarySeint.vsReportes.classes;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "kpi_atenciones")
public class KpiAtenciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate fecha;

    private Integer atencionesSolicitadas = 0;
    private Integer atencionesConfirmadas = 0;
    private Integer atencionesEnEspera = 0;
    private Integer atencionesCerradas = 0;
    private Integer atencionesCanceladas = 0;

    private Double tiempoPromedioEsperaMinutos = 0.0;
    private Integer cantidadMedicionesEspera = 0;

    public KpiAtenciones() {}

    public Long getId() { return id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Integer getAtencionesSolicitadas() { return atencionesSolicitadas; }
    public void setAtencionesSolicitadas(Integer atencionesSolicitadas) { this.atencionesSolicitadas = atencionesSolicitadas; }

    public Integer getAtencionesConfirmadas() { return atencionesConfirmadas; }
    public void setAtencionesConfirmadas(Integer atencionesConfirmadas) { this.atencionesConfirmadas = atencionesConfirmadas; }

    public Integer getAtencionesEnEspera() { return atencionesEnEspera; }
    public void setAtencionesEnEspera(Integer atencionesEnEspera) { this.atencionesEnEspera = atencionesEnEspera; }

    public Integer getAtencionesCerradas() { return atencionesCerradas; }
    public void setAtencionesCerradas(Integer atencionesCerradas) { this.atencionesCerradas = atencionesCerradas; }

    public Integer getAtencionesCanceladas() { return atencionesCanceladas; }
    public void setAtencionesCanceladas(Integer atencionesCanceladas) { this.atencionesCanceladas = atencionesCanceladas; }

    public Double getTiempoPromedioEsperaMinutos() { return tiempoPromedioEsperaMinutos; }
    public void setTiempoPromedioEsperaMinutos(Double tiempoPromedioEsperaMinutos) { this.tiempoPromedioEsperaMinutos = tiempoPromedioEsperaMinutos; }

    public Integer getCantidadMedicionesEspera() { return cantidadMedicionesEspera; }
    public void setCantidadMedicionesEspera(Integer cantidadMedicionesEspera) { this.cantidadMedicionesEspera = cantidadMedicionesEspera; }
}