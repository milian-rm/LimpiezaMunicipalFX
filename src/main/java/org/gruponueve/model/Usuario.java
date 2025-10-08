package org.gruponueve.model;

public class Usuario {
    private Integer idUsuario;
    private String correo;
    private String contrasenia;

    public Usuario() {
    }

    public Usuario(Integer idUsuario, String correo, String contrasenia) {
        this.idUsuario = idUsuario;
        this.correo = correo;
        this.contrasenia = contrasenia;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
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

    @Override
    public String toString() {
        return "Usuario [idUsuario=" + idUsuario + ", correo=" + correo + ", contrasenia=" + contrasenia + "]";
    }
    
}
