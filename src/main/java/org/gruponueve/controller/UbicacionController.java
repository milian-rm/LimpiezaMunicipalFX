/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.gruponueve.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.gruponueve.database.Conexion;
import org.gruponueve.model.Municipalidad;
import org.gruponueve.model.Ubicacion;
import org.gruponueve.system.Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class UbicacionController implements Initializable {
    private Main principal;
    private ObservableList<Ubicacion> listaUbicaciones;
    private Ubicacion modeloUbicacion;

    private enum EstadoFormulario {
        AGREGAR, EDITAR, ELIMINAR, NINGUNO
    };
    EstadoFormulario estadoActual = EstadoFormulario.NINGUNO;

    @FXML
    private TableView<Ubicacion> tablaUbicaciones;

    @FXML
    private TableColumn colID, colZona, colColonia,
            colCalle, colAvenida, colMunicipalidad;

    @FXML
    private TextField txtBuscar, txtId, txtZona, txtColonia,
            txtCalle, txtAvenida;

    @FXML
    private ComboBox<Municipalidad> cbxMunicipalidad;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo,
            btnEliminar, btnEditar,
            btnVolver, btnBuscar;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public void volver() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumna();
        cargarTabla();
        cargarMunicipalidadComboBox();
        tablaUbicaciones.setOnMouseClicked(event -> cargarEnTextoField());
        txtBuscar.setOnAction(eh -> BuscarTabla());
        
    }


    public void configurarColumna() {
        colID.setCellValueFactory(new PropertyValueFactory<Ubicacion, Integer>("idUbicacion"));
        colZona.setCellValueFactory(new PropertyValueFactory<Ubicacion, String>("zona"));
        colColonia.setCellValueFactory(new PropertyValueFactory<Ubicacion, String>("colonia"));
        colCalle.setCellValueFactory(new PropertyValueFactory<Ubicacion, String>("calle"));
        colAvenida.setCellValueFactory(new PropertyValueFactory<Ubicacion, String>("avenida"));
        colMunicipalidad.setCellValueFactory(new PropertyValueFactory<Ubicacion, String>("idMunicipalidad"));
    }

    public void cargarTabla() {
        listaUbicaciones = FXCollections.observableList(ListarTabla());
        tablaUbicaciones.setItems(listaUbicaciones);
        tablaUbicaciones.getSelectionModel().selectFirst();
        if (tablaUbicaciones.getSelectionModel().getSelectedItem() != null) {
            cargarEnTextoField();
        }
    }
 
    private ArrayList<Municipalidad> cargarModeloMunicipalidad() {
        ArrayList<Municipalidad> municipalidades = new ArrayList<>();
        try {
            CallableStatement cs = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarMunicipalidades();");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                municipalidades.add(new Municipalidad(
                        rs.getInt("idMunicipalidad"),
                        rs.getString("Municipalidad")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return municipalidades;
    }

    private void cargarMunicipalidadComboBox() {
        ObservableList<Municipalidad> listaMunicipalidades = FXCollections.observableArrayList(cargarModeloMunicipalidad());
        cbxMunicipalidad.setItems(listaMunicipalidades);
    }

    public void cargarEnTextoField() {
        Ubicacion ubicacionSeleccionada = tablaUbicaciones.getSelectionModel().getSelectedItem();
        if (ubicacionSeleccionada != null) {
            txtId.setText(String.valueOf(ubicacionSeleccionada.getIdUbicacion()));
            txtZona.setText(ubicacionSeleccionada.getZona());
            txtColonia.setText(ubicacionSeleccionada.getColonia());
            txtCalle.setText(ubicacionSeleccionada.getCalle());
            txtAvenida.setText(ubicacionSeleccionada.getAvenida());
        }
        for (Municipalidad m : cbxMunicipalidad.getItems()) {
                if (m.getIdMunicipalidad()== ubicacionSeleccionada.getIdMunicipalidad()) {
                    cbxMunicipalidad.setValue(m);
                    break;
                }
            }
    }

    public ArrayList<Ubicacion> ListarTabla() {
        ArrayList<Ubicacion> ubicaciones = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarUbicaciones();");
            ResultSet resultado = enunciado.executeQuery();

            while (resultado.next()) {
                ubicaciones.add(new Ubicacion(
                    resultado.getInt("idUbicacion"),
                    resultado.getString("zona"),
                    resultado.getString("colonia"),
                    resultado.getString("calle"),
                    resultado.getString("avenida"),
                    resultado.getInt("idMunicipalidad")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar de MySQL la entidad Ubicaciones: " + ex.getMessage());
            ex.printStackTrace();
        }
        return ubicaciones;
    }

    public Ubicacion obtenerModeloUbicacion() {
        int idUbicacion = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());
        String zona = txtZona.getText();
        String colonia = txtColonia.getText();
        String calle = txtCalle.getText();
        String avenida = txtAvenida.getText();

        Municipalidad muniSeleccionada = (Municipalidad) cbxMunicipalidad.getValue();
        
        int idMunicipalidad = muniSeleccionada.getIdMunicipalidad();

        return new Ubicacion(idUbicacion, zona, colonia, calle, avenida, idMunicipalidad);
    }


    public void agregarUbicacion() {
        modeloUbicacion = obtenerModeloUbicacion();
        System.out.println("Agregando ubicación: " + modeloUbicacion);
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_AgregarUbicacion(?,?,?,?,?);");
            enunciado.setString(1, modeloUbicacion.getZona());
            enunciado.setString(2, modeloUbicacion.getColonia());
            enunciado.setString(3, modeloUbicacion.getCalle());
            enunciado.setString(4, modeloUbicacion.getAvenida());
            enunciado.setInt(5, modeloUbicacion.getIdMunicipalidad());
            enunciado.executeQuery();
            cargarTabla();
        } catch (SQLException e) {
            System.out.println("Error al agregar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarUbicacion() {
        modeloUbicacion = obtenerModeloUbicacion();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarUbicacion(?,?,?,?,?,?);");
            enunciado.setInt(1, modeloUbicacion.getIdUbicacion());
            enunciado.setString(2, modeloUbicacion.getZona());
            enunciado.setString(3, modeloUbicacion.getColonia());
            enunciado.setString(4, modeloUbicacion.getCalle());
            enunciado.setString(5, modeloUbicacion.getAvenida());
            enunciado.setInt(6, modeloUbicacion.getIdMunicipalidad());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarUbicacion() {
        modeloUbicacion = obtenerModeloUbicacion();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarUbicacion(?);");
            enunciado.setInt(1, modeloUbicacion.getIdUbicacion());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }

    public void limpiarFormulario() {
        txtId.clear();
        txtZona.clear();
        txtColonia.clear();
        txtCalle.clear();
        txtAvenida.clear();
        cbxMunicipalidad.setValue(null);
    }

    public void estadoFormulario(EstadoFormulario est) {
        estadoActual = est;
        boolean activo = (est == EstadoFormulario.AGREGAR || est == EstadoFormulario.EDITAR);

        txtZona.setDisable(!activo);
        txtColonia.setDisable(!activo);
        txtCalle.setDisable(!activo);
        txtAvenida.setDisable(!activo);
        cbxMunicipalidad.setDisable(!activo);

        tablaUbicaciones.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        //btnVolver.setDisable(activo);
        btnEditar.setDisable(activo);
        btnAnterior.setDisable(activo);
        btnSiguiente.setDisable(activo);
        btnNuevo.setText(activo ? "Guardar": "Agregar");
        btnEliminar.setText(activo ? "Cancelar":"Eliminar");
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaUbicaciones.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaUbicaciones.getSelectionModel().select(indice - 1);
            cargarEnTextoField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaUbicaciones.getSelectionModel().getSelectedIndex();
        if (indice < listaUbicaciones.size() - 1) {
            tablaUbicaciones.getSelectionModel().select(indice + 1);
            cargarEnTextoField();
        }
    }

    @FXML
    private void nuevaUbicacion(){
        switch (estadoActual) {
            case NINGUNO:
                limpiarFormulario();
                System.out.println("Voy a crear un registro para ubicaciones");
                estadoFormulario(EstadoFormulario.AGREGAR);
                break;
            case AGREGAR:
                agregarUbicacion();
                System.out.println("Voy a guardar los datos ingresados");
                estadoFormulario(EstadoFormulario.NINGUNO);
                break;
            case EDITAR:
                actualizarUbicacion();
                System.out.println("Voy a guardar edición indicada");
                estadoFormulario(EstadoFormulario.NINGUNO);
                break;
        }
    }
    
    @FXML
    private void editarUbicacion(){
        estadoFormulario(EstadoFormulario.EDITAR);
    }
    
    @FXML
    private void cancelarEliminar(){
        if(estadoActual == EstadoFormulario.NINGUNO){
            System.out.println("Voy a eliminar el registro");
            eliminarUbicacion();
        }else{
            estadoFormulario(EstadoFormulario.NINGUNO);
        }
    }

    @FXML
    private void BuscarTabla() {
        String texto = txtBuscar.getText().toLowerCase();
        ArrayList<Ubicacion> resultadoBusqueda = new ArrayList<>();
        for (Ubicacion ubicacion : listaUbicaciones) {
            if (String.valueOf(ubicacion.getIdMunicipalidad()).contains(texto) ||
                ubicacion.getZona().toLowerCase().contains(texto) ||
                ubicacion.getColonia().toLowerCase().contains(texto) ||
                ubicacion.getCalle().toLowerCase().contains(texto) ||
                String.valueOf(ubicacion.getIdMunicipalidad()).contains(texto)) {
                resultadoBusqueda.add(ubicacion);
            }
        }
        tablaUbicaciones.setItems(FXCollections.observableList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaUbicaciones.getSelectionModel().selectFirst();
        }
    }
}
