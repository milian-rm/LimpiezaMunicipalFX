/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.gruponueve.controller;
/*
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
/*
public class ReporteController implements Initializable {
    
    protected Main principal;
    private ObservableList<Reporte> listaReportes;
    private Reporte modeloReporte;
    private enum EstadoFormulario {AGREGAR, EDITAR, ELIMINAR, NINGUNA}
    EstadoFormulario estadoActual = EstadoFormulario.NINGUNA;

    @FXML
    protected TableView<Reporte> tablaReporte;
    
    @FXML
    protected TableColumn colId, colNombres, colApellidos, colTelefono,
            colSalario, colRol;
    
    @FXML
    private TextField txtId, txtNombres, txtApellidos, txtTelefono,
            txtBuscar;
    
    @FXML
    private Spinner<Double> spSalario;
    
    @FXML
    private RadioButton rbReportel, rbSupervisor, rbAlcaldeAux, 
            rbAlcaldeMun;
    
    @FXML
    private Button btnBuscar, btnAnterior, btnSiguiente, btnAgregar, btnActualizar, btnEliminar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        configurarSpinner();
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
        colNombres.setCellValueFactory(new PropertyValueFactory<Reporte, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Reporte, String>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Reporte, String>("telefono"));
        colSalario.setCellValueFactory(new PropertyValueFactory<Reporte, Double>("salario"));
        colRol.setCellValueFactory(new PropertyValueFactory<Reporte, Rol>("rol"));
    }
    
    private void configurarSpinner(){
        SpinnerValueFactory<Double> valor = 
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 0);
        spSalario.setValueFactory(valor);
    }
    
    private ArrayList<Reporte> listarReportes(){
        ArrayList<Reporte> reportes = new ArrayList<>();
        try {
//            Connection conexion = Conexion.getInstancia().getConexion();
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarReporte();");
            ResultSet resultado = enunciado.executeQuery();
            while(resultado.next()){
                reportes.add(new Reporte(
                            resultado.getInt("idReporte"),
                            resultado.getString("nombres"),
                            resultado.getString("apellidos"),
                            resultado.getString("telefono"),
                            resultado.getDouble("salario"),
                            Rol.valueOf(resultado.getString("rol").toUpperCase())));
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
            txtNombres.setText(reporte.getNombres());
            txtApellidos.setText(reporte.getApellidos());
            txtTelefono.setText(reporte.getTelefono());
        }
        spSalario.getValueFactory().setValue(reporte.getSalario());
        
        if (String.valueOf(reporte.getRol()).equalsIgnoreCase("PERSONAL")) {
            rbReportel.setSelected(true);
        }else if(String.valueOf(reporte.getRol()).equalsIgnoreCase("SUPERVISOR")){
            rbSupervisor.setSelected(true);
        }else if(String.valueOf(reporte.getRol()).equalsIgnoreCase("ALCALDE_AUXILIAR")){
            rbAlcaldeAux.setSelected(true);
        }else if(String.valueOf(reporte.getRol()).equalsIgnoreCase("ALCALDE_MUNICIPAL")){
            rbAlcaldeMun.setSelected(true);
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
        
        String rol = "";
        if (rbReportel.isSelected()) {
            rol = "PERSONAL";
        } else if (rbSupervisor.isSelected()) {
            rol = "SUPERVISOR";
        } else if (rbAlcaldeAux.isSelected()) {
            rol = "ALCALDE_AUXILIAR";
        } else if (rbAlcaldeMun.isSelected()) {
            rol = "ALCALDE_MUNICIPAL";
        }
        return new Reporte(
                codigoReporte, 
                txtNombres.getText(), 
                txtApellidos.getText(), 
                txtTelefono.getText(), 
                spSalario.getValue(),
                Rol.valueOf(rol));
    }
   
    
    
    public void agregarModelo(){
        modeloReporte = cargarModeloReporte();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_AgregarReporte(?,?,?,?);");
            enunciado.setString(1, modeloReporte.getNombres());
            enunciado.setString(2, modeloReporte.getApellidos());
            enunciado.setString(3, modeloReporte.getTelefono());
            enunciado.setDouble(4, modeloReporte.getSalario());
            int registrosAgregados = enunciado.executeUpdate();
            if(registrosAgregados > 0){
                System.out.println("Reporte agregada");
                cargarTablaModelos();
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar una Reporte");
            e.printStackTrace();
        }
        
    }
    
    public void actualizarModelo(){
        modeloReporte = cargarModeloReporte();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarReporte(?,?,?,?,?,?);");
            enunciado.setInt(1, modeloReporte.getIdReporte());
            enunciado.setString(2, modeloReporte.getNombres());
            enunciado.setString(3, modeloReporte.getApellidos());
            enunciado.setString(4, modeloReporte.getTelefono());
            enunciado.setDouble(5, modeloReporte.getSalario());
            enunciado.setString(6, String.valueOf(modeloReporte.getRol()));
            enunciado.execute();
            cargarTablaModelos();
        } catch (SQLException e) {
            System.out.println("Error al editar una reporte");
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
        Double reinicio = 0.0;
        txtId.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtTelefono.clear();
        spSalario.getValueFactory().setValue(reinicio); 
    }
    private void actualizarEstadoFormulario(EstadoFormulario estado){
        estadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.EDITAR);
        txtNombres.setDisable(!activo);
        txtApellidos.setDisable(!activo);
        txtTelefono.setDisable(!activo);
        spSalario.setDisable(!activo);
        
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
            if(m.getNombres().toLowerCase().contains(nombre)){
                resultadoBusqueda.add(m);
            }
        }
        tablaReporte.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if(!resultadoBusqueda.isEmpty()){
            tablaReporte.getSelectionModel().selectFirst();
        }
    }
    
}
*/