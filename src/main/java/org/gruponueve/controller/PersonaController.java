/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.gruponueve.controller;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.gruponueve.database.Conexion;
import org.gruponueve.model.Persona;
import org.gruponueve.model.Rol;
import org.gruponueve.system.Main;

/**
 *
 * @author Roberto
 */
public class PersonaController implements Initializable{
    
    protected Main principal;
    
    private ObservableList<Persona> listaPersonas;

    @FXML
    protected TableView<Persona> tablaPersona;
    
    @FXML
    protected TableColumn colId, colNombres, colApellidos, colTelefono, colCorreo,
            colSalario, colRol;
    
    @FXML
    private TextField txtId, txtNombres, txtApellidos, txtTelefono, txtCorreo, txtContrasenia;
    
    @FXML
    private Spinner<Double> spSalario;
    
    @FXML
    private RadioButton rbPersonal, rbSupervisor, rbUsuario, rbAlcaldeAux, 
            rbAlcaldeMun;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void setPrincipal(Main principal) {
        this.principal = principal;
    }

    public Main getPrincipal() {
        return principal;
    }
    
    public void escenaMenuPrincipal(){
        principal.menuPrincipal();
    }

    private void configurarColumnas(){
        colId.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("idPersona"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Persona, String>("apellido"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Persona, String>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<Persona, String>("correo"));
        colSalario.setCellValueFactory(new PropertyValueFactory<Persona, Double>("Salario"));
        colRol.setCellValueFactory(new PropertyValueFactory<Persona, Rol>("Rol"));
    }
    
    private void configurarSpinner(){
        SpinnerValueFactory<Double> valor = 
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 0);
        spSalario.setValueFactory(valor);
    }
    
    private ArrayList<Persona> listarPersonas(){
        ArrayList<Persona> personas = new ArrayList<>();
        try {
//            Connection conexion = Conexion.getInstancia().getConexion();
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarPersonas();");
            ResultSet resultado = enunciado.executeQuery();
            while(resultado.next()){
                personas.add(new Persona(
                            resultado.getInt("idPersona"),
                            resultado.getString("nombres"),
                            resultado.getString("apellidos"),
                            resultado.getString("telefono"),
                            resultado.getString("correo"),
                            resultado.getDouble("salario"),
                            Rol.valueOf(resultado.getString("rol").toUpperCase())));
                        }
        } catch (SQLException ex) {
            System.out.println("Error al cargar de MySQL las Personas");
            ex.printStackTrace();
        }
       return personas;
    }
    private void cargarPersonaFormulario(){
        Persona persona = tablaPersona.getSelectionModel().getSelectedItem();
        if (persona != null) {
            txtId.setText(String.valueOf(persona.getIdPersona()));
            txtNombres.setText(persona.getNombres());
            txtApellidos.setText(persona.getApellidos());
            txtTelefono.setText(persona.getTelefono());
            txtCorreo.setText(persona.getCorreo());
        }
        spSalario.getValueFactory().setValue(persona.getSalario());
        
        if (String.valueOf(persona.getRol()).equalsIgnoreCase("PERSONAL")) {
            rbPersonal.setSelected(true);
        }else if(String.valueOf(persona.getRol()).equalsIgnoreCase("SUPERVISOR")){
            rbSupervisor.setSelected(true);
        }else if(String.valueOf(persona.getRol()).equalsIgnoreCase("ALCALDE_AUXILIAR")){
            rbAlcaldeAux.setSelected(true);
        }else if(String.valueOf(persona.getRol()).equalsIgnoreCase("ALCALDE_MUNICIPAL")){
            rbAlcaldeMun.setSelected(true);
        }else if(String.valueOf(persona.getRol()).equalsIgnoreCase("USUARIO")){
            rbUsuario.setSelected(true);
        }    
    }
    
   
    public void cargarTablaModelos(){
        listaPersonas = FXCollections.observableArrayList(listarPersonas());
        tablaPersona.setItems(listaPersonas);
        tablaPersona.getSelectionModel().selectFirst();
        cargarPersonaFormulario();
    }
    
    private Persona cargarModeloPersona(){
        int codigoPersona = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());
        
        String rol = "";
        if (rbPersonal.isSelected()) {
            rol = "PERSONAL";
        } else if (rbSupervisor.isSelected()) {
            rol = "SUPERVISOR";
        } else if (rbAlcaldeAux.isSelected()) {
            rol = "ALCALDE_AUXILIAR";
        } else if (rbAlcaldeMun.isSelected()) {
            rol = "ALCALDE_MUNICIPAL";
        } else if (rbUsuario.isSelected()) {
            rol = "USUARIO";
        }
        return new Persona(
                codigoPersona, 
                txtNombres.getText(), 
                txtApellidos.getText(), 
                txtTelefono.getText(), 
                txtCorreo.getText(), 
                spSalario.getValue(),
                Double.parseDouble(txtPrecio.getText()), 
                dpFechaVencimiento.getValue(),                 
                codigoProveedor);
    }
    
    
    private ArrayList<Proveedor> cargarModeloProveedor(){
        ArrayList<Proveedor> proveedores = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_ListarProveedores();");
            ResultSet resultado = enunciado.executeQuery();
            while(resultado.next()){
                Proveedor p = new Proveedor(
                        resultado.getInt(1), 
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getString(4),
                        resultado.getString(5));
                proveedores.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proveedores;
    }
    
    private void cargarProveedor(){
        ObservableList<Proveedor> proveedores = FXCollections.observableArrayList(cargarModeloProveedor());
        cbxProveedores.setItems(proveedores);
    }
    
    
    public void agregarModelo(){
        modeloPersona = cargarModeloPersona();
        Proveedor proveedorSeleccionado = cbxProveedores.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_AgregarPersona(?,?,?,?,?,?);");
            enunciado.setInt(1, modeloPersona.getIdProveedor());
            enunciado.setString(2, modeloPersona.getNombre());
            enunciado.setString(3, modeloPersona.getDescripcion());
            enunciado.setInt(4, modeloPersona.getStock());
            enunciado.setDouble(5, modeloPersona.getPrecioUnitario());
            enunciado.setDate(6, Date.valueOf(modeloPersona.getFechaVencimiento()));
            int registrosAgregados = enunciado.executeUpdate();
            if(registrosAgregados > 0){
                System.out.println("Persona agregado");
                cargarTablaModelos();
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar un Persona");
            e.printStackTrace();
        }
        
    }
    
    public void actualizarModelo(){
        modeloPersona = cargarModeloPersona();
        Proveedor proveedorSeleccionado = cbxProveedores.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarPersona(?,?,?,?,?,?,?);");
            enunciado.setInt(1, modeloPersona.getIdProducto());
            enunciado.setInt(2, modeloPersona.getIdProveedor());
            enunciado.setString(3, modeloPersona.getNombre());
            enunciado.setString(4, modeloPersona.getDescripcion());
            enunciado.setInt(5, modeloPersona.getStock());
            enunciado.setDouble(6, modeloPersona.getPrecioUnitario());
            enunciado.setDate(7, Date.valueOf(modeloPersona.getFechaVencimiento()));
            enunciado.execute();
            cargarTablaModelos();
        } catch (SQLException e) {
            System.out.println("Error al editar una persona");
            e.printStackTrace();
        }
        
    }
    public void eliminarModelo(){
        modeloPersona = tablaPersonas.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarPersona(?);");
            enunciado.setInt(1, modeloPersona.getIdProducto());
            enunciado.execute();
            cargarTablaModelos();
        } catch (SQLException e) {
            System.out.println("Error al eliminar una Persona");
            e.printStackTrace();
        }
    }
    private void limpiarCampos(){
        Integer reiniciar = 0;
        txtCodigoPersona.clear();
        txtNombre.clear();
        txtDescripcion.clear();
        spStock.getValueFactory().setValue(reiniciar);
        txtPrecio.clear();
        //txtSexo.clear();
        dpFechaVencimiento.setValue(null);
        cbxProveedores.getSelectionModel().clearSelection(); 
    }
    private void actualizarEstadoFormulario(EstadoFormulario estado){
        estadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.EDITAR);
        txtNombre.setDisable(!activo);
        txtDescripcion.setDisable(!activo);
        spStock.setDisable(!activo);
        txtPrecio.setDisable(!activo);
        dpFechaVencimiento.setDisable(!activo);
        cbxProveedores.setDisable(!activo);
        
        tablaPersonas.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);
        
        btnAgregar.setText(activo ? "Guardar":"Nuevo");
        btnEliminar.setText(activo ? "Cancelar":"Eliminar");
        btnActualizar.setDisable(activo);
        
    }
    
    @FXML
    private void btnVolverAction(){
      int indice = tablaPersonas.getSelectionModel().getSelectedIndex();
      if(indice > 0){
          tablaPersonas.getSelectionModel().select(indice-1);
      }
      cargarPersonaFormulario();
    }
    
    @FXML
    private void btnSiguienteAction(){
      int indice = tablaPersonas.getSelectionModel().getSelectedIndex();
      if(indice < listaPersonas.size()-1){
          tablaPersonas.getSelectionModel().select(indice+1);
      }
      cargarPersonaFormulario();
    }
    
    @FXML
    private void agregarPersona(){
        switch (estadoActual) {
            case NINGUNA:
                limpiarCampos();
                System.out.println("Voy a crear un registro para personas");
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
    private void editarPersona(){
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
        ArrayList<Persona> resultadoBusqueda = new ArrayList<>();
        for(Persona m:listaPersonas){
            if(m.getNombre().toLowerCase().contains(nombre)){
                resultadoBusqueda.add(m);
            }
        }
        tablaPersonas.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if(!resultadoBusqueda.isEmpty()){
            tablaPersonas.getSelectionModel().selectFirst();
        }
    }
    
}
