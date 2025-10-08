package org.gruponueve.model;

import java.util.Date;

public class OrdenLimpieza {
    private int idOrden;
    private Date horarioInicio;
    private Date horarioCierre;
    private int idReporte;
    
    public OrdenLimpieza() {
    }

    public OrdenLimpieza(int idOrden, Date horarioInicio, Date horarioCierre, int idReporte) {
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

    public Date getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(Date horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public Date getHorarioCierre() {
        return horarioCierre;
    }

    public void setHorarioCierre(Date horarioCierre) {
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
        return "OrdenLimpieza [idOrden=" + idOrden + ", horarioInicio=" + horarioInicio + ", horarioCierre="
                + horarioCierre + ", idReporte=" + idReporte + "]";
    }

    
}
