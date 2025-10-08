/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gruponueve.model;

/**
 *
 * @author Roberto
 */
public class Grupo {
    int idGrupoPersonas, idOrden, idPersona;

    public Grupo() {
    }

    public Grupo(int idGrupoPersonas, int idOrden, int idPersona) {
        this.idGrupoPersonas = idGrupoPersonas;
        this.idOrden = idOrden;
        this.idPersona = idPersona;
    }

    public int getIdGrupoPersonas() {
        return idGrupoPersonas;
    }

    public void setIdGrupoPersonas(int idGrupoPersonas) {
        this.idGrupoPersonas = idGrupoPersonas;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }
    
    
}
