/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.gruponueve.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.gruponueve.system.Main;

/**
 * FXML Controller class
 *
 * @author Roberto
 */
public class MenuAlcaldeController implements Initializable {
    
    private Main principal;
    
    public void setPrincipal(Main principal){
        this.principal = principal;
    }
    
    public Main getPrincipal(){
        return principal;
    }
    
    public void escenaInicio(){
        principal.inicioSesion();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void reporte(){
        principal.reporte();
    }
    public void ordenes(){
        principal.ordenLimpieza();
    }
    public void asignarGrupo(){
        principal.asignacionGrupo();
    }
    public void grupo(){
        principal.grupo();
    }
    public void personal(){
        principal.persona();
    }
    public void municipalidad(){
        principal.municipalidad();
    }
    public void usuarios(){
        principal.usuario();
    }
    public void ubicaciones(){
        principal.ubicacion();
    }
    
}
