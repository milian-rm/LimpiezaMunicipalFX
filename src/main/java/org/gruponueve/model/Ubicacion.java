package org.gruponueve.model;

public class Ubicacion {
    private int idUbicacion;
    private String zona;
    private String colonia;
    private String calle;
    private String avenida;
    private String idMunicipalidad;

    public Ubicacion() {
    }

    public Ubicacion(int idUbicacion, String zona, String colonia, String calle, String avenida, String idMunicipalidad) {
        this.idUbicacion = idUbicacion;
        this.zona = zona;
        this.colonia = colonia;
        this.calle = calle;
        this.avenida = avenida;
        this.idMunicipalidad = idMunicipalidad;
    }

    public int getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getAvenida() {
        return avenida;
    }

    public void setAvenida(String avenida) {
        this.avenida = avenida;
    }

    public String getIdMunicipalidad() {
        return idMunicipalidad;
    }

    public void setIdMunicipalidad(String idMunicipalidad) {
        this.idMunicipalidad = idMunicipalidad;
    }

    @Override
    public String toString() {
        return "Ubicacion [idUbicacion=" + idUbicacion + ", zona=" + zona + ", colonia=" + colonia + ", calle=" + calle
                + ", avenida=" + avenida + ", idMunicipalidad=" + idMunicipalidad + "]";
    }

}
