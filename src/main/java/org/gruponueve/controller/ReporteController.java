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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.gruponueve.database.Conexion;
import org.gruponueve.model.Reporte;
import org.gruponueve.model.Rol;
import org.gruponueve.system.Main;

/**
 * FXML Controller class
 *
 * @author informatica
 */

public class ReporteController implements Initializable {
    
    protected Main principal;
    private ObservableList<Reporte> listaReportes;
    private Reporte modeloReporte;
    private enum EstadoFormulario {AGREGAR, EDITAR, ELIMINAR, NINGUNA}
    EstadoFormulario estadoActual = EstadoFormulario.NINGUNA;

    @FXML
    protected TableView<Reporte> tablaReporte;
    
    @FXML
    protected TableColumn colId, colNombre, colTelefono,colDescripcion, colEstado,
            colIdUbicacion;
    
    @FXML
    private TextField txtId, txtNombre, txtTelefono, txtDescripcion,
            txtBuscar;
    
    @FXML
    private RadioButton rbPendiente, rbEnProceso, rbTerminado;
    
    @FXML
    private ComboBox<Ubicacion> cbxUbicaciones;
    
    @FXML
    private Button btnBuscar, btnAnterior, btnSiguiente, btnAgregar, btnActualizar, btnEliminar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarTablaModelos();
        // expresiones lambda el metodo 
        tablaReporte.setOnMouseClicked(eventHandler -> cargarReporteFormulario());
    }
    
    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public Main getPrincipal() {
        return principal;
    }
    
    public void escenaMenuPrincipal(){
        //principal.();
    }

    private void configurarColumnas(){
        colId.setCellValueFactory(new PropertyValueFactory<Reporte, Integer>("idReporte"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Reporte, String>("nombrePersona"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Reporte, String>("telefono"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Reporte, String>("descripcion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Reporte, String>("estado"));
        colIdUbicacion.setCellValueFactory(new PropertyValueFactory<Reporte, Integer>("idUbicacion"));        
    }
    
    private ArrayList<Reporte> listarReportes(){
        ArrayList<Reporte> reportes = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarReporte();");
            ResultSet resultado = enunciado.executeQuery();
            while(resultado.next()){
                reportes.add(new Reporte(
                            resultado.getInt("idReporte"),
                            resultado.getInt("idUbicacion"),
                            resultado.getString("nombrePersona"),
                            resultado.getString("telefono"),
                            resultado.getString("descripcion"),
                            resultado.getString("estado")));
                        }
        } catch (SQLException ex) {
            System.out.println("Error al cargar de MySQL las Reportes");
            ex.printStackTrace();
        }
       return reportes;
    }
    private void cargarReporteFormulario(){
        Reporte reporte = tablaReporte.getSelectionModel().getSelectedItem();
        if (reporte != null) {
            txtId.setText(String.valueOf(reporte.getIdReporte()));
            txtNombre.setText(reporte.getNombrePersona());
            txtTelefono.setText(reporte.getTelefono());
            txtDescripcion.setText(reporte.getDescripcion());
        }
        for (Ubicacion u: cbxUbicaciones.getItems()) {
            if (u.getIdModelo()== reporte.getIdUbicación()) {
                cbxUbicaciones.setValue(u);
                break;
            }
        }
        
        if (String.valueOf(reporte.getEstado()).equalsIgnoreCase("Pendiente")) {
            rbPendiente.setSelected(true);
        }else if(String.valueOf(reporte.getEstado()).equalsIgnoreCase("En Proceso")){
            rbEnProceso.setSelected(true);
        }else if(String.valueOf(reporte.getEstado()).equalsIgnoreCase("Terminado")){
            rbTerminado.setSelected(true);
        }
    }
    
   
    public void cargarTablaModelos(){
        listaReportes = FXCollections.observableArrayList(listarReportes());
        tablaReporte.setItems(listaReportes);
        tablaReporte.getSelectionModel().selectFirst();
        cargarReporteFormulario();
    }
    
    private Reporte cargarModeloReporte(){
        int codigoReporte = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());
        
        Ubicacion ubicacionSeleccionada = cbxUbicaciones.getSelectionModel().getSelectedItem();
        int codigoUbicacion = ubicacionSeleccionada != null ? ubicacionSeleccionada.getIdModelo(): 0;
        
        String estado = "";
        if (rbPendiente.isSelected()) {
            estado = "Pendiente";
        } else if (rbEnProceso.isSelected()) {
            estado = "En Proceso";
        } else if (rbTerminado.isSelected()) {
            estado = "Terminado";
        }
        return new Reporte(
                codigoReporte,
                codigoUbicacion,
                txtNombre.getText(), 
                txtTelefono.getText(), 
                txtDescripcion.getText(), 
                estado);
    }
   
    private ArrayList<Ubicacion> cargarModeloUbicacion(){
        ArrayList<Ubicacion> ubicaciones = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_ListarUbicaciones();");
            ResultSet resultado = enunciado.executeQuery();
            while(resultado.next()){
                Ubicacion u = new Ubicacion(
                        resultado.getInt(1), 
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getString(4),
                        resultado.getString(5),
                        resultado.getInt(6));
                ubicaciones.add(u);
            }
        } catch (SQLException a) {
            a.printStackTrace();
        }
        return ubicaciones;
    }
    
    private void cargarUbicacion(){
        ObservableList<Ubicacion> ubicaciones = FXCollections.observableArrayList(cargarModeloUbicacion());
        cbxUbicaciones.setItems(ubicaciones);
    }
    
    
    public void agregarModelo(){
        modeloReporte = cargarModeloReporte();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_AgregarReporte(?,?,?,?,?);");
            enunciado.setString(1, modeloReporte.getNombrePersona());
            enunciado.setString(2, modeloReporte.getTelefono());
            enunciado.setString(3, modeloReporte.getDescripcion());
            enunciado.setString(4, modeloReporte.getEstado());
            enunciado.setInt(5, modeloReporte.getIdUbicación());
            int registrosAgregados = enunciado.executeUpdate();
            if(registrosAgregados > 0){
                System.out.println("Reporte agregado");
                cargarTablaModelos();
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar un Reporte");
            e.printStackTrace();
        }
        
    }
    
    public void actualizarModelo(){
        modeloReporte = cargarModeloReporte();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarReporte(?,?,?,?,?,?);");
            enunciado.setInt(1, modeloReporte.getIdReporte());
            enunciado.setString(2, modeloReporte.getNombrePersona());
            enunciado.setString(3, modeloReporte.getTelefono());
            enunciado.setString(4, modeloReporte.getDescripcion());
            enunciado.setString(5, modeloReporte.getEstado());
            enunciado.setInt(6, modeloReporte.getIdUbicación());
            enunciado.execute();
            cargarTablaModelos();
        } catch (SQLException e) {
            System.out.println("Error al editar un reporte");
            e.printStackTrace();
        }
        
    }
    public void eliminarModelo(){
        modeloReporte = tablaReporte.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarReporte(?);");
            enunciado.setInt(1, modeloReporte.getIdReporte());
            enunciado.execute();
            cargarTablaModelos();
        } catch (SQLException e) {
            System.out.println("Error al eliminar una Reporte");
            e.printStackTrace();
        }
    }
    private void limpiarCampos(){
        txtId.clear();
        txtNombre.clear();
        txtDescripcion.clear();
        txtTelefono.clear();
        cbxUbicaciones.getSelectionModel().clearSelection(); 
    }
    private void actualizarEstadoFormulario(EstadoFormulario estado){
        estadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.EDITAR);
        txtNombre.setDisable(!activo);
        txtDescripcion.setDisable(!activo);
        txtTelefono.setDisable(!activo);
        rbPendiente.setDisable(!activo);
        rbEnProceso.setDisable(!activo);
        rbTerminado.setDisable(!activo);
        cbxUbicaciones.setDisable(!activo);
        
        tablaReporte.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);
        
        btnAgregar.setText(activo ? "Guardar":"Nuevo");
        btnEliminar.setText(activo ? "Cancelar":"Eliminar");
        btnActualizar.setDisable(activo);
        
    }
    
    @FXML
    private void btnVolverAction(){
      int indice = tablaReporte.getSelectionModel().getSelectedIndex();
      if(indice > 0){
          tablaReporte.getSelectionModel().select(indice-1);
      }
      cargarReporteFormulario();
    }
    
    @FXML
    private void btnSiguienteAction(){
      int indice = tablaReporte.getSelectionModel().getSelectedIndex();
      if(indice < listaReportes.size()-1){
          tablaReporte.getSelectionModel().select(indice+1);
      }
      cargarReporteFormulario();
    }
    
    @FXML
    private void agregarReporte(){
        switch (estadoActual) {
            case NINGUNA:
                limpiarCampos();
                System.out.println("Voy a crear un registro para reportes");
                actualizarEstadoFormulario(EstadoFormulario.AGREGAR);
                break;
            case AGREGAR:
                agregarModelo();
                System.out.println("Voy a guardar los datos ingresados");
                actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
                break;
            case EDITAR:
                actualizarModelo();
                System.out.println("Voy a guardar edición indicada");
                actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
                break;
        }
    }
    
    @FXML
    private void editarReporte(){
        actualizarEstadoFormulario(EstadoFormulario.EDITAR);
    }
    @FXML
    private void cancelarEliminar(){
        if(estadoActual == EstadoFormulario.NINGUNA){
            System.out.println("Voy a eliminar el registro");
            eliminarModelo();
        }else{
            actualizarEstadoFormulario(EstadoFormulario.NINGUNA);
        }
    }
    @FXML
    private void buscarPorNombre(){
        String nombre = txtBuscar.getText().toLowerCase();
        ArrayList<Reporte> resultadoBusqueda = new ArrayList<>();
        for(Reporte m:listaReportes){
            if(m.getNombrePersona().toLowerCase().contains(nombre)){
                resultadoBusqueda.add(m);
            }
        }
        tablaReporte.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if(!resultadoBusqueda.isEmpty()){
            tablaReporte.getSelectionModel().selectFirst();
        }
    }
    
}
