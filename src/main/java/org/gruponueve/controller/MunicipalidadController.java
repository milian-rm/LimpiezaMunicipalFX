package org.gruponueve.controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.gruponueve.model.Municipalidad;
import org.gruponueve.system.Main;

import com.mysql.jdbc.CallableStatement;

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

public class MunicipalidadController implements Initializable{

    private Main principal;
    private ObservableList<Municipalidad> listaMunicipalidad;
    private Municipalidad modeloMunicipalidad;

    private enum EstadoFormulario {
        AGREGAR, EDITAR, ELIMINAR, NINGUNO
    };
    EstadoFormulario estadoActual = EstadoFormulario.NINGUNO;

    @FXML
    private TableView<Municipalidad> tablaMunicipalidades;

    @FXML
    private TableColumn colID, colMuni;

    @FXML
    private TextField txtBuscar, txtId, txtMunicipalidad;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo,
            btnEliminar, btnEditar, btnCancelar,
            btnGuardar, btnVolver, btnBuscar;

    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public void volver() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumna();
        cargarTabla();
        tablaMunicipalidades.setOnMouseClicked(event -> cargarEnTextoField());
        txtBuscar.setOnAction(eh -> BuscarTabla());
    }


    public void configurarColumna() {
        colID.setCellValueFactory(new PropertyValueFactory<Municipalidad, Integer>("idMunicipalidad"));
        colMuni.setCellValueFactory(new PropertyValueFactory<Municipalidad, String>("Municipalidad"));    }

    public void cargarTabla() {
        listaMunicipalidad = FXCollections.observableList(ListarTabla());
        tablaMunicipalidades.setItems(listaMunicipalidad);
        tablaMunicipalidades.getSelectionModel().selectFirst();
        if (tablaMunicipalidades.getSelectionModel().getSelectedItem() != null) {
            cargarEnTextoField();
        }
    }

    public void cargarEnTextoField() {
        Municipalidad muniSeleccionado = tablaMunicipalidades.getSelectionModel().getSelectedItem();
        txtId.setText(String.valueOf(muniSeleccionado.getIdMunicipalidad()));
        txtMunicipalidad.setText(muniSeleccionado.getMunicipalidad());
    }

    public ArrayList<Municipalidad> ListarTabla() {
        ArrayList<Municipalidad> municipalidades = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarMunicipalidades();");
            ResultSet resultado = enunciado.executeQuery();

            while (resultado.next()) {
                municipalidades.add(new Municipalidad(
                        resultado.getInt("idMunicipalidad"),
                        resultado.getString("Municipalidad")));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar de MySQL: " + ex.getMessage());
            ex.printStackTrace();
        }
        return municipalidades;
    }

    public Municipalidad obtenerModeloMunicipalidad() {
        int idMuni = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());
        String muni = txtMunicipalidad.getText();

        return new Municipalidad(idMuni, muni);
    }


    public void agregarMunicipalidad() {
        modeloMunicipalidad = obtenerModeloMunicipalidad();
        System.out.println("Agregando Municipalidad: " + modeloMunicipalidad);
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_AgregarMunicipalidad(?);");
            enunciado.setString(1, modeloMunicipalidad.getMunicipalidad());
            enunciado.executeQuery();
            cargarTabla();
        } catch (SQLException e) {
            System.out.println("Error al agregar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarMunicipalidad() {
        modeloMunicipalidad = obtenerModeloMunicipalidad();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarMunicipalidad(?,?);");
            enunciado.setInt(1, modeloMunicipalidad.getIdMunicipalidad());
            enunciado.setString(2, modeloMunicipalidad.getMunicipalidad());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarMunicipalidad() {
        modeloMunicipalidad = obtenerModeloMunicipalidad();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarMunicipalidad(?);");
            enunciado.setInt(1, modeloMunicipalidad.getIdMunicipalidad());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }

    public void limpiarFormulario() {
        txtId.clear();
        txtMunicipalidad.clear();
    }

    public void estadoFormulario(EstadoFormulario est) {
        estadoActual = est;
        boolean activo = (est == EstadoFormulario.AGREGAR || est == EstadoFormulario.EDITAR);

        txtMunicipalidad.setDisable(!activo);
        btnGuardar.setDisable(!activo);
        btnCancelar.setDisable(!activo);

        tablaMunicipalidades.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        btnVolver.setDisable(activo);
        btnEditar.setDisable(activo);
        btnAnterior.setDisable(activo);
        btnSiguiente.setDisable(activo);
        btnNuevo.setDisable(activo);
        btnEliminar.setDisable(activo);
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaMunicipalidades.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaMunicipalidades.getSelectionModel().select(indice - 1);
            cargarEnTextoField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaMunicipalidades.getSelectionModel().getSelectedIndex();
        if (indice < listaMunicipalidad.size() - 1) {
            tablaMunicipalidades.getSelectionModel().select(indice + 1);
            cargarEnTextoField();
        }
    }

    @FXML
    private void btnNuevoAction() {
        limpiarFormulario();
        estadoActual = EstadoFormulario.AGREGAR;
        estadoFormulario(EstadoFormulario.AGREGAR);
    }

    @FXML
    private void btnEditarAction() {
        estadoFormulario(EstadoFormulario.EDITAR);
    }

    @FXML
    private void btnEliminarAction() {
        eliminarMunicipalidad();
        cargarTabla();
    }

    @FXML
    private void btnCancelarAction() {
        if (tablaMunicipalidades.getSelectionModel().getSelectedItem() != null) {
            cargarEnTextoField();
        }
        estadoFormulario(EstadoFormulario.NINGUNO);
    }

    @FXML
    private void btnGuardarAction() {
        System.out.println("Guardar botón presionado. Estado actual: " + estadoActual);
        if (estadoActual == EstadoFormulario.AGREGAR) {
            agregarMunicipalidad();
        } else if (estadoActual == EstadoFormulario.EDITAR) {
            actualizarMunicipalidad();
        }
        estadoFormulario(EstadoFormulario.NINGUNO);
    }

    
    @FXML
    private void BuscarTabla() {
        String texto = txtBuscar.getText().toLowerCase();
        ArrayList<Municipalidad> resultadoBusqueda = new ArrayList<>();
        for (Municipalidad muni : listaMunicipalidad) {
            if (String.valueOf(muni.getIdMunicipalidad()).contains(texto) ||
                muni.getMunicipalidad().toLowerCase().contains(texto)) {
                resultadoBusqueda.add(muni);
            }
        }
        tablaMunicipalidades.setItems(FXCollections.observableList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaMunicipalidades.getSelectionModel().selectFirst();
        }
    }

    
}
