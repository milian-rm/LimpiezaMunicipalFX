package org.gruponueve.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.gruponueve.database.Conexion;
import org.gruponueve.model.OrdenLimpieza;
import org.gruponueve.model.Reporte;
import org.gruponueve.system.Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class OrdenLimpiezaController implements Initializable {
    private Main principal;
    private ObservableList<OrdenLimpieza> listaOrdenLimpiezas;
    private OrdenLimpieza modeloOrdenLimpieza;

    private enum EstadoFormulario {
        AGREGAR, EDITAR, ELIMINAR, NINGUNO
    };
    EstadoFormulario estadoActual = EstadoFormulario.NINGUNO;

    @FXML
    private TableView<OrdenLimpieza> tablaOrdenes;

    @FXML
    private TableColumn colID, colHorarioI, colHorarioC,
            colReporte;

    @FXML
    private TextField txtBuscar, txtId;

    @FXML
    private DatePicker dpHorarioI, dpHorarioC;

    @FXML
    private ComboBox<Reporte> cbxReporte;

    @FXML
    private Button btnAnterior, btnSiguiente, btnNuevo,
            btnEliminar, btnEditar,
            btnVolver, btnBuscar;

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

    public void volver(){
        if(principal.getRol().equals("Personal")){
            menuPersonal();
        }else if(principal.getRol().equals("Supervisor")){
            menuSupervisor();
        }else if(principal.getRol().equals("Alcalde auxiliar")|| principal.getRol().equals("Alcalde municipal")){
            menuAlcalde();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumna();
        cargarTabla();
        cargarReporteComboBox();
        tablaOrdenes.setOnMouseClicked(event -> cargarEnTextoField());
        txtBuscar.setOnAction(eh -> BuscarTabla());
        
    }

    public void configurarColumna() {
        colID.setCellValueFactory(new PropertyValueFactory<OrdenLimpieza, Integer>("idOrden"));
        colHorarioI.setCellValueFactory(new PropertyValueFactory<OrdenLimpieza, String>("horarioInicio"));
        colHorarioC.setCellValueFactory(new PropertyValueFactory<OrdenLimpieza, String>("horarioCierre"));
        colReporte.setCellValueFactory(new PropertyValueFactory<OrdenLimpieza, Integer>("idReporte"));
    }

    public void cargarTabla() {
        listaOrdenLimpiezas = FXCollections.observableList(ListarTabla());
        tablaOrdenes.setItems(listaOrdenLimpiezas);
        tablaOrdenes.getSelectionModel().selectFirst();
        if (tablaOrdenes.getSelectionModel().getSelectedItem() != null) {
            cargarEnTextoField();
        }
    }
 
    private ArrayList<Reporte> cargarModeloReporte() {
        ArrayList<Reporte> reportes = new ArrayList<>();
        try {
            CallableStatement cs = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarReportes();");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                reportes.add(new Reporte(
                    rs.getInt("idReporte"),
                    rs.getInt("idUbicacion"),
                    rs.getString("estado"),
                    rs.getString("nombrePersona"),
                    rs.getString("telefono"),
                    rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportes;
    }

    private void cargarReporteComboBox() {
        ObservableList<Reporte> listaReportes = FXCollections.observableArrayList(cargarModeloReporte());
        cbxReporte.setItems(listaReportes);
    }

    public void cargarEnTextoField() {
        OrdenLimpieza ordenSeleccionada = tablaOrdenes.getSelectionModel().getSelectedItem();
        if (ordenSeleccionada != null) {
            txtId.setText(String.valueOf(ordenSeleccionada.getIdOrden()));
            dpHorarioI.setValue(ordenSeleccionada.getHorarioInicio());
            dpHorarioC.setValue(ordenSeleccionada.getHorarioCierre());
        }
        for (Reporte r : cbxReporte.getItems()) {
            if (r.getIdReporte() == ordenSeleccionada.getIdReporte()) {
                cbxReporte.setValue(r);
                break;
            }
        }
    }

    public ArrayList<OrdenLimpieza> ListarTabla() {
        ArrayList<OrdenLimpieza> ordenes = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarOrdenesLimpieza();");
            ResultSet resultado = enunciado.executeQuery();

            while (resultado.next()) {
                ordenes.add(new OrdenLimpieza(
                    resultado.getInt("idOrden"),
                    resultado.getDate("horarioInicio").toLocalDate(),
                    resultado.getDate("horarioCierre").toLocalDate(),
                    resultado.getInt("idReporte")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar de MySQL la entidad Ordenes de Limpieza: " + ex.getMessage());
            ex.printStackTrace();
        }
        return ordenes;
    }

    public OrdenLimpieza obtenerModeloOrdenLimpieza() {
        int idOrden = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());
        LocalDate horarioInicio = dpHorarioI.getValue();
        LocalDate horarioCierre = dpHorarioC.getValue();

        Reporte reporteSeleccionado = (Reporte) cbxReporte.getValue();

        int idReporte = reporteSeleccionado.getIdReporte();

        return new OrdenLimpieza(idOrden, horarioInicio, horarioCierre, idReporte);
    }


    public void agregarOrden() {
        modeloOrdenLimpieza = obtenerModeloOrdenLimpieza();
        System.out.println("Agregando orden: " + modeloOrdenLimpieza);
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_AgregarOrdenLimpieza(?,?,?);");
            enunciado.setDate(1, java.sql.Date.valueOf(modeloOrdenLimpieza.getHorarioInicio()));
            enunciado.setDate(2, java.sql.Date.valueOf(modeloOrdenLimpieza.getHorarioCierre()));
            enunciado.setInt(3, modeloOrdenLimpieza.getIdReporte());
            enunciado.executeQuery();
            cargarTabla();
        } catch (SQLException e) {
            System.out.println("Error al agregar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarOrden() {
        modeloOrdenLimpieza = obtenerModeloOrdenLimpieza();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarOrdenLimpieza(?,?,?,?);");
            enunciado.setInt(1, modeloOrdenLimpieza.getIdOrden());
            enunciado.setDate(2, java.sql.Date.valueOf(modeloOrdenLimpieza.getHorarioInicio()));
            enunciado.setDate(3, java.sql.Date.valueOf(modeloOrdenLimpieza.getHorarioCierre()));
            enunciado.setInt(4, modeloOrdenLimpieza.getIdReporte());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarOrden() {
        modeloOrdenLimpieza = obtenerModeloOrdenLimpieza();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarOrdenLimpieza(?);");
            enunciado.setInt(1, modeloOrdenLimpieza.getIdOrden());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }

    public void limpiarFormulario() {
        txtId.clear();
        dpHorarioI.setValue(null);
        dpHorarioC.setValue(null);
        cbxReporte.setValue(null);
    }

    public void estadoFormulario(EstadoFormulario est) {
        if(principal.getRol().equals("Personal")){
            btnNuevo.setDisable(true);
            btnEliminar.setDisable(true);
            btnEditar.setDisable(true);
        }else{
        estadoActual = est;
        boolean activo = (est == EstadoFormulario.AGREGAR || est == EstadoFormulario.EDITAR);

        dpHorarioI.setDisable(!activo);
        dpHorarioC.setDisable(!activo);
        cbxReporte.setDisable(!activo);

        tablaOrdenes.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        //btnVolver.setDisable(activo);
        btnEditar.setDisable(activo);
        btnAnterior.setDisable(activo);
        btnSiguiente.setDisable(activo);
        btnNuevo.setText(activo ? "Guardar": "Agregar");
        btnEliminar.setText(activo ? "Cancelar":"Eliminar");
        }
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaOrdenes.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaOrdenes.getSelectionModel().select(indice - 1);
            cargarEnTextoField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaOrdenes.getSelectionModel().getSelectedIndex();
        if (indice < listaOrdenLimpiezas.size() - 1) {
            tablaOrdenes.getSelectionModel().select(indice + 1);
            cargarEnTextoField();
        }
    }

    @FXML
    private void nuevaOrden(){
        switch (estadoActual) {
            case NINGUNO:
                limpiarFormulario();
                System.out.println("Voy a crear un registro para ordenes");
                estadoFormulario(EstadoFormulario.AGREGAR);
                break;
            case AGREGAR:
                agregarOrden();
                System.out.println("Voy a guardar los datos ingresados");
                estadoFormulario(EstadoFormulario.NINGUNO);
                break;
            case EDITAR:
                actualizarOrden();
                System.out.println("Voy a guardar edición indicada");
                estadoFormulario(EstadoFormulario.NINGUNO);
                break;
        }
    }
    
    @FXML
    private void editarOrden(){
        estadoFormulario(EstadoFormulario.EDITAR);
    }
    
    @FXML
    private void cancelarEliminar(){
        if(estadoActual == EstadoFormulario.NINGUNO){
            System.out.println("Voy a eliminar el registro");
            eliminarOrden();
        }else{
            estadoFormulario(EstadoFormulario.NINGUNO);
        }
    }

    @FXML
    private void BuscarTabla() {
        String texto = txtBuscar.getText().toLowerCase();
        ArrayList<OrdenLimpieza> resultadoBusqueda = new ArrayList<>();
        for (OrdenLimpieza orden : listaOrdenLimpiezas) {
            if (String.valueOf(orden.getIdOrden()).contains(texto) ||
                orden.getHorarioInicio().equals(texto) ||
                orden.getHorarioCierre().equals(texto) ||
                String.valueOf(orden.getIdReporte()).contains(texto)) {
                resultadoBusqueda.add(orden);
            }
        }
        tablaOrdenes.setItems(FXCollections.observableList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaOrdenes.getSelectionModel().selectFirst();
        }
    }
}
