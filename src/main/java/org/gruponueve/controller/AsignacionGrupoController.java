/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.gruponueve.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.gruponueve.database.Conexion;
import org.gruponueve.model.Grupo;
import org.gruponueve.model.OrdenLimpieza;
import org.gruponueve.model.Persona;

/**
 * FXML Controller class
 *
 * @author Roberto
 */

public class AsignacionGrupoController extends PersonaController implements Initializable {

    private GrupoController grupoController;

//    private Main principal;
//
//    public void setPrincipal(Main principal) {
//        this.principal = principal;
//    }

    @FXML
    private TableView<Grupo> tablaDetalleTemporal;
    @FXML
    private TableColumn colIdOrden, colIdPersonaG;

    private ObservableList<Grupo> listaGrupo = FXCollections.observableArrayList();

    @FXML
    private ComboBox<OrdenLimpieza> cbxOrdenes;


    public void escenaMenuPrincipal() {
        principal.menuPrincipal();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.inicializarPersonas();
        configurarColumnasAsignacion();
        cargarOrdenLimpieza();
        
        tablaDetalleTemporal.setItems(listaGrupo);

        tablaPersona.setOnMouseClicked(e -> {
            if (estadoActual == EstadoFormulario.NINGUNA) {
                Persona personaSeleccionado = tablaPersona.getSelectionModel().getSelectedItem();
                
            }
        });
    }

    private void configurarColumnasAsignacion() {
        colIdPersonaG.setCellValueFactory(new PropertyValueFactory<>("idPersona"));
        colIdOrden.setCellValueFactory(new PropertyValueFactory<>("idOrden"));
//        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombres"));
//        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
    }

    public void limpiarCarrito() {
        listaGrupo.clear();
    }

    private ArrayList<OrdenLimpieza> cargarModeloOrdenLimpieza(){
        ArrayList<OrdenLimpieza> ordenes = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_ListarOrdenesLimpieza();");
            ResultSet resultado = enunciado.executeQuery();
            while(resultado.next()){
                OrdenLimpieza o = new OrdenLimpieza(
                        resultado.getInt(1), 
                        resultado.getDate(2).toLocalDate(),
                        resultado.getDate(3).toLocalDate(),
                        resultado.getInt(4));
                ordenes.add(o);
            }
        } catch (SQLException a) {
            a.printStackTrace();
        }
        return ordenes;
    }
    
    private void cargarOrdenLimpieza(){
        ObservableList<OrdenLimpieza> ordenes = FXCollections.observableArrayList(cargarModeloOrdenLimpieza());
        cbxOrdenes.setItems(ordenes);
    }
    
    private Grupo obtenerDatos() {
        Persona personaSeleccionado = tablaPersona.getSelectionModel().getSelectedItem();
        if (personaSeleccionado == null) {
            System.out.println("Por favor, selecciona una persona.");
            return null;
        }

        OrdenLimpieza ordenSeleccionada = cbxOrdenes.getSelectionModel().getSelectedItem();
        if (ordenSeleccionada == null) {
            System.out.println("Por favor, selecciona una orden.");
            return null;
        }
        return new Grupo(
            ordenSeleccionada.getIdOrden(),
            personaSeleccionado.getIdPersona());
    }
    
    @FXML
    public void agregarDetalle() {
        Grupo grupo = obtenerDatos();
        if (grupo == null) {
            return;
        }

        try (CallableStatement cs = Conexion.getInstancia().getConexion()
                .prepareCall("call sp_AgregarGrupoPersona(?,?);")) {
            cs.setInt(1, grupo.getIdOrden());
            cs.setInt(2, grupo.getIdPersona());
            cs.execute();
            listaGrupo.add(grupo);
            tablaDetalleTemporal.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void eliminarDetalle() {
         Grupo detalleSeleccionado = tablaDetalleTemporal.getSelectionModel().getSelectedItem();
         
        if(detalleSeleccionado == null) {
            System.out.println("Debe seleccionar un detalle para eliminar.");
            return;
        }
         
        int idRegistroDetalle = detalleSeleccionado.getIdGrupoPersonas();

        try (CallableStatement cs = Conexion.getInstancia().getConexion()
                .prepareCall("call sp_EliminarGrupoPersona(?);")) {
            cs.setInt(1, idRegistroDetalle);
            cs.execute();

            listaGrupo.remove(detalleSeleccionado);
            tablaDetalleTemporal.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void finalizarCompra() {
        
    }

    
}