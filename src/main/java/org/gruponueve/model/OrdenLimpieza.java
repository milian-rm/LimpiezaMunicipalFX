package org.gruponueve.model;

import java.time.LocalDate;
import java.util.Date;

public class OrdenLimpieza {
    private int idOrden;
    private LocalDate horarioInicio;
    private LocalDate horarioCierre;
    private int idReporte;
    
    public OrdenLimpieza() {
    }

    public OrdenLimpieza(int idOrden, LocalDate horarioInicio, LocalDate horarioCierre, int idReporte) {
        this.idOrden = idOrden;
        this.horarioInicio = horarioInicio;
        this.horarioCierre = horarioCierre;
        this.idReporte = idReporte;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public LocalDate getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalDate horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalDate getHorarioCierre() {
        return horarioCierre;
    }

    public void setHorarioCierre(LocalDate horarioCierre) {
        this.horarioCierre = horarioCierre;
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    @Override
    public String toString() {
        return "idOrden=" + idOrden + ", horarioInicio=" + horarioInicio + ", horarioCierre=" + horarioCierre;
    }
    
    

    
}
