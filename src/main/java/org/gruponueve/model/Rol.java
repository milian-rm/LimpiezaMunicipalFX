/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gruponueve.model;

public enum Rol {
    PERSONAL("Personal"),
    SUPERVISOR("Supervisor"),
    ALCALDE_AUXILIAR("Alcalde auxiliar"),
    ALCALDE_MUNICIPAL("Alcalde municipal");
    
    private final String valor;
    
    Rol(String valor) {
        this.valor = valor;
    }
    
    public String getValor() {
        return valor;
    }
    
    // Método para convertir desde la base de datos
    public static Rol fromString(String texto) {
        for (Rol rol : Rol.values()) {
            if (rol.valor.equalsIgnoreCase(texto)) {
                return rol;
            }
        }
        throw new IllegalArgumentException("Rol no encontrado: " + texto);
    }
    
    @Override
    public String toString() {
        return valor;
    }
}
