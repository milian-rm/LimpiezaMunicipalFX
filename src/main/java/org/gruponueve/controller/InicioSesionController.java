package org.gruponueve.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.gruponueve.database.Conexion;
import org.gruponueve.model.Usuario;
import org.gruponueve.system.Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class InicioSesionController implements Initializable {
    private Main principal;
    private Usuario user;

    @FXML
    private Label lblAdvertencia; 
    @FXML TextField txtfldCorreo;
    @FXML PasswordField PssfldContraseña;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }
    
    public void modeloUsuario(){
        user = new Usuario();
        String correo = txtfldCorreo.getText();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_BuscarUsuarioPorCorreo(?);");
            enunciado.setString(1,correo);
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
    
    public void IngresarMenu(){
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
                    //principal.MenuPrincipal();
                    System.out.println("Bienvenido " + user.getCorreo());
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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtfldCorreo.requestFocus();
        txtfldCorreo.setOnAction(eh -> PssfldContraseña.requestFocus());
        PssfldContraseña.setOnAction(eh -> IngresarMenu());
    }
}
