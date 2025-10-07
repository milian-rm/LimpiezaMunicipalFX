package org.gruponueve.model;

public class Municipalidad {
    private Integer idMunicipalidad;
    private String Municipalidad;
    
    public Municipalidad(Integer idMunicipalidad, String municipalidad) {
        this.idMunicipalidad = idMunicipalidad;
        Municipalidad = municipalidad;
    }

    public Municipalidad() {
    }

    public Integer getIdMunicipalidad() {
        return idMunicipalidad;
    }

    public void setIdMunicipalidad(Integer idMunicipalidad) {
        this.idMunicipalidad = idMunicipalidad;
    }

    public String getMunicipalidad() {
        return Municipalidad;
    }

    public void setMunicipalidad(String municipalidad) {
        Municipalidad = municipalidad;
    }

    @Override
    public String toString() {
        return "Municipalidad [idMunicipalidad=" + idMunicipalidad + ", Municipalidad=" + Municipalidad + "]";
    }

}
