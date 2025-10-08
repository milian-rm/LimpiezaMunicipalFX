/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gruponueve.model;

/**
 *
 * @author informatica
 */
public class Reporte {
    int idReporte, idUbicación;
    String nombrePersona, telefono, descripcion, estado;

    public Reporte(int idReporte, int idUbicación, String nombrePersona, String telefono, String descripcion, String estado) {
        this.idReporte = idReporte;
        this.idUbicación = idUbicación;
        this.nombrePersona = nombrePersona;
        this.telefono = telefono;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public Reporte() {
    }

    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public int getIdUbicación() {
        return idUbicación;
    }

    public void setIdUbicación(int idUbicación) {
        this.idUbicación = idUbicación;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
}
