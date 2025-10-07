/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gruponueve.model;

/**
 *
 * @author Roberto
 */
public class Persona {
    int idPersona;
    String nombres, apellidos, telefono, correo, contrasenia;
    Double salario;
    Rol rol;

    public Persona() {
    }

    public Persona(int idPersona, String nombres, String apellidos, String telefono, String correo, String contrasenia, Double salario, Rol rol) {
        this.idPersona = idPersona;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.salario = salario;
        this.rol = rol;
    }

    public Persona(int idPersona, String nombres, String apellidos, String telefono, String correo, Double salario, Rol rol) {
        this.idPersona = idPersona;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.salario = salario;
        this.rol = rol;
    }
    

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "ID=" + idPersona + ", Nombres=" + nombres + ", Apellidos=" + apellidos + ", Rol=" + rol;
    }
    
    
    
}
