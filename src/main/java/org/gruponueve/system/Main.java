/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.gruponueve.system;

import java.util.logging.Logger;

import org.gruponueve.controller.MunicipalidadController;
import org.gruponueve.controller.UsuarioController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.gruponueve.controller.PersonaController;
import org.gruponueve.controller.UbicacionController;

/**
 *
 * @author josel
 */
public class Main extends Application{
    private Stage stage;
    private static int ANCHO = 1500;
    private static int ALTO = 800;
    private static String URL = "/view/";
    public static void main(String[] args) {
        System.out.println("Prueba funcionamiento");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        ubicacion();
        /*FXMLLoader cargador = new FXMLLoader(getClass().getResource(
                "/view/PersonaView.fxml"));
        Parent raiz = cargador.load();
        Scene escena = new Scene(raiz);
        stage.setScene(escena);
        stage.setMaximized(true);*/
        
        stage.show();
    }

    public FXMLLoader cambioEscena(String fxml){
        FXMLLoader cargardorFXML = null;
        try {
            cargardorFXML = new FXMLLoader(getClass().getResource(URL+fxml));
            Parent archivoFXML = cargardorFXML.load();
            Scene escena = new Scene(archivoFXML,ANCHO,ALTO);
            stage.setScene(escena);
            //Prueba
            stage.setMaximized(true);
            //----
            stage.setTitle("MuniGuate");
        } catch (Exception e) {
            System.out.println("ERROR AL CAMBIAR " + e.getMessage());
            e.printStackTrace();
        }
        return cargardorFXML;
    }
    
    public void menuPrincipal(){
        //ControllerMenuPrincipal cmp = cambioEscena("VistaMenuPrincipal.fxml").getController();
        //cmp.setPrincipal(this);
    }
    
    public void persona(){
        PersonaController pc = cambioEscena("PersonaView.fxml").getController();
        pc.setPrincipal(this);
    }

    public void municipalidad(){
        try{
            MunicipalidadController mc = (MunicipalidadController) cambioEscena("MunicipalidadView.fxml").getController();
            mc.setPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error al cargar la vista: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void usuario(){
        try{
            UsuarioController uc = (UsuarioController) cambioEscena("UsuarioView.fxml").getController();
            uc.setPrincipal(this);
        } catch (Exception e) {
            System.out.println("Error al cargar la vista: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void ubicacion(){
        try{
            UbicacionController uc = (UbicacionController) cambioEscena("UbicacionView.fxml").getController();
            uc.setPrincipal(this);
        } catch(Exception e){
            System.out.println("Error al cargar la vista: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
