package org.gruponueve.controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.gruponueve.system.Main;

/**
 * FXML Controller class
 *
 * @author Roberto
 */
public class MenuPersonalController implements Initializable {
    
    private Main principal;
    
    public void setPrincipal(Main principal){
        this.principal = principal;
    }
    
    public Main getPrincipal(){
        return principal;
    }
    public void reporte(){
        principal.reporte();
    }
    public void ordenes(){
        principal.ordenLimpieza();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void ubicaciones(){
        principal.ubicacion();
    }
    
    public void escenaInicio(){
        principal.inicioSesion();
    }
    
    
}
