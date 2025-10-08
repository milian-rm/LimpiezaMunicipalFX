package org.gruponueve.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.gruponueve.database.Conexion;
import org.gruponueve.model.Usuario;
import org.gruponueve.system.Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.gruponueve.model.Persona;
import org.gruponueve.model.Rol;

public class InicioSesionController implements Initializable {

    private Main principal;
    private Usuario user;
    private Persona persona;

    @FXML
    private Label lblAdvertencia;
    @FXML
    TextField txtfldCorreo;
    @FXML
    PasswordField PssfldContraseña;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    public void menuPersonal(){
        principal.menuPrincipalPersonal();
    }
    
    public void menuSupervisor(){
        principal.menuPrincipalSupervisor();
    }
    public void menuAlcalde(){
        principal.menuPrincipalAlcalde();
    }

    public void modeloUsuario() {
        user = new Usuario();
        String correo = txtfldCorreo.getText();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_BuscarUsuarioPorCorreo(?);");
            enunciado.setString(1, correo);
            ResultSet resultado = enunciado.executeQuery();
            if (resultado.next()) {
                user = new Usuario(
                        resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getString(3));
            }
        } catch (SQLException ex) {
            System.out.println("Error al guardar usuario " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void cargarModeloPersona() {
        persona = new Persona();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_ListarPersona();");
            ResultSet resultado = enunciado.executeQuery();
            if (resultado.next()) {
                persona = new Persona(
                        resultado.getInt(1),
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getString(4),
                        resultado.getDouble(5),
                        Rol.valueOf(resultado.getString(6).toUpperCase().replace(" ", "_")),
                        resultado.getInt(7));
            }
        } catch (SQLException a) {
            a.printStackTrace();
        }
    }

    public void IngresarMenu() {
        user = new Usuario();
        //Comprobacion de credencial de contraseñas
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_loginUsuario(?,?);");
            enunciado.setString(1, txtfldCorreo.getText());
            enunciado.setString(2, PssfldContraseña.getText());
            ResultSet resultado = enunciado.executeQuery();
            if (resultado.next()) {
                String mensaje = resultado.getString("mensaje");

                if ("TRUE".equals(mensaje)) {
                    modeloUsuario(); 

                    System.out.println("ID Usuario: " + user.getIdUsuario());
                    System.out.println("Bienvenido " + user.getCorreo());

                    obtenerRol(user.getIdUsuario()); // ← CAMBIO AQUÍ: usa idUsuario
                } else {
                    System.out.println("FALSE");
                    lblAdvertencia.setVisible(true);
                    System.out.println("Error al iniciar sesión: " + mensaje);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void obtenerRol(int id){
        persona = new Persona();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_BuscarPersonalPorIdUser(?);");
            enunciado.setInt(1, id);
            ResultSet resultado = enunciado.executeQuery();
            if (resultado.next()) {
                String mensaje = resultado.getString(1);
                
                if ("Personal".equals(mensaje)) {
                    System.out.println("Usted es " + mensaje);
                    principal.setRol(mensaje);
                    menuPersonal();
                } else if("Supervisor".equals(mensaje)){
                    System.out.println("Usted es " + mensaje);
                    principal.setRol(mensaje);
                    menuSupervisor();
                } else if("Alcalde auxiliar".equals(mensaje)||"Alcalde municipal".equals(mensaje)){
                    System.out.println("Usted es " + mensaje);
                    principal.setRol(mensaje);
                    menuAlcalde();
                } else{
                    System.out.println("NO EXISTE");
                    principal.setRol(null);
                }
                
                System.out.println(principal.getRol());
            }
        } catch (Exception e) {
            System.out.println("Error al buscar rol " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtfldCorreo.requestFocus();
        txtfldCorreo.setOnAction(eh -> PssfldContraseña.requestFocus());
        PssfldContraseña.setOnAction(eh -> IngresarMenu());
    }
    
    
}
