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
import org.gruponueve.model.Persona;
import org.gruponueve.model.Rol;
import org.gruponueve.model.Usuario;
import org.gruponueve.system.Main;

/**
 *
 * @author Roberto
 */
public class PersonaController implements Initializable{
    
    protected Main principal;
    private ObservableList<Persona> listaPersonas;
    private Persona modeloPersona;
    public enum EstadoFormulario {AGREGAR, EDITAR, ELIMINAR, NINGUNA}
    EstadoFormulario estadoActual = EstadoFormulario.NINGUNA;

    @FXML
    protected TableView<Persona> tablaPersona;
    
    @FXML
    protected TableColumn colId, colNombres, colApellidos, colTelefono,
            colSalario, colRol, colIdUsuario;
    
    @FXML
    private TextField txtId, txtNombres, txtApellidos, txtTelefono,
            txtBuscar;
    
    @FXML
    private Spinner<Double> spSalario;
    
    @FXML
    private ComboBox<Usuario> cbxUsuarios;
    
    @FXML
    private RadioButton rbPersonal, rbSupervisor, rbAlcaldeAux, 
            rbAlcaldeMun;
    
    @FXML
    private Button btnBuscar, btnAnterior, btnSiguiente, btnAgregar, btnActualizar, btnEliminar;

    //Método para poder mostrar la tabla en la vista de Asignación de Personal
    public void inicializarPersonas() {
    configurarColumnas();
    cargarPersonasVista();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        configurarSpinner();
        cargarUsuario();
        cargarTablaModelos();
        // expresiones lambda el metodo 
        tablaPersona.setOnMouseClicked(eventHandler -> cargarPersonaFormulario());
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
        colId.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("idPersona"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Persona, String>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Persona, String>("telefono"));
        colSalario.setCellValueFactory(new PropertyValueFactory<Persona, Double>("salario"));
        colRol.setCellValueFactory(new PropertyValueFactory<Persona, Rol>("rol"));
        colIdUsuario.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("idUsuario"));
    }
    
    private void configurarSpinner(){
        SpinnerValueFactory<Double> valor = 
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000000, 0, 100);
        spSalario.setValueFactory(valor);
    }
    
    private ArrayList<Persona> listarPersonas(){
        ArrayList<Persona> personas = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarPersona();");
            ResultSet resultado = enunciado.executeQuery();
            while(resultado.next()){
                personas.add(new Persona(
                            resultado.getInt("idPersona"),
                            resultado.getString("nombres"),
                            resultado.getString("apellidos"),
                            resultado.getString("telefono"),
                            resultado.getDouble("salario"),
                            Rol.valueOf(resultado.getString("rol").toUpperCase()),
                            resultado.getInt("idUsuario")));
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
            spSalario.getValueFactory().setValue(persona.getSalario());

            for (Usuario u: cbxUsuarios.getItems()) {
                if (u.getIdUsuario()== persona.getIdUsuario()) {
                    cbxUsuarios.setValue(u);
                    break;
                }
            }
        }
        
        if (String.valueOf(persona.getRol()).equalsIgnoreCase("PERSONAL")) {
            rbPersonal.setSelected(true);
        }else if(String.valueOf(persona.getRol()).equalsIgnoreCase("SUPERVISOR")){
            rbSupervisor.setSelected(true);
        }else if(String.valueOf(persona.getRol()).equalsIgnoreCase("ALCALDE_AUXILIAR")){
            rbAlcaldeAux.setSelected(true);
        }else if(String.valueOf(persona.getRol()).equalsIgnoreCase("ALCALDE_MUNICIPAL")){
            rbAlcaldeMun.setSelected(true);
        }
    }
    
    protected void cargarPersonasVista() {
        listaPersonas = FXCollections.observableArrayList(listarPersonas());
        tablaPersona.setItems(listaPersonas);
    }
    
   
    public void cargarTablaModelos(){
        listaPersonas = FXCollections.observableArrayList(listarPersonas());
        tablaPersona.setItems(listaPersonas);
        tablaPersona.getSelectionModel().selectFirst();
        cargarPersonaFormulario();
    }
    
    private Persona cargarModeloPersona(){
        int codigoPersona = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());
        
        Usuario usuarioSeleccionado = cbxUsuarios.getSelectionModel().getSelectedItem();
        int codigoUsuario = usuarioSeleccionado != null ? usuarioSeleccionado.getIdUsuario(): 0;
        
        String rol = "";
        if (rbPersonal.isSelected()) {
            rol = "PERSONAL";
        } else if (rbSupervisor.isSelected()) {
            rol = "SUPERVISOR";
        } else if (rbAlcaldeAux.isSelected()) {
            rol = "ALCALDE_AUXILIAR";
        } else if (rbAlcaldeMun.isSelected()) {
            rol = "ALCALDE_MUNICIPAL";
        }
        return new Persona(
                codigoPersona, 
                txtNombres.getText(), 
                txtApellidos.getText(), 
                txtTelefono.getText(), 
                spSalario.getValue(),
                Rol.valueOf(rol),
                codigoUsuario);
    }
    
    private ArrayList<Usuario> cargarModeloUsuario(){
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_ListarUsuarios();");
            ResultSet resultado = enunciado.executeQuery();
            while(resultado.next()){
                Usuario u = new Usuario(
                        resultado.getInt(1), 
                        resultado.getString(2),
                        resultado.getString(3));
                usuarios.add(u);
            }
        } catch (SQLException a) {
            a.printStackTrace();
        }
        return usuarios;
    }
   
    private void cargarUsuario(){
        ObservableList<Usuario> usuarios = FXCollections.observableArrayList(cargarModeloUsuario());
        cbxUsuarios.setItems(usuarios);
    }
    
    
    public void agregarModelo(){
        modeloPersona = cargarModeloPersona();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_AgregarPersona(?,?,?,?,?);");
            enunciado.setString(1, modeloPersona.getNombres());
            enunciado.setString(2, modeloPersona.getApellidos());
            enunciado.setString(3, modeloPersona.getTelefono());
            enunciado.setDouble(4, modeloPersona.getSalario());
            enunciado.setInt(5, modeloPersona.getIdUsuario());
            int registrosAgregados = enunciado.executeUpdate();
            if(registrosAgregados > 0){
                System.out.println("Persona agregada");
                cargarTablaModelos();
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar una Persona");
            e.printStackTrace();
        }
        
    }
    
    public void actualizarModelo(){
        modeloPersona = cargarModeloPersona();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarPersona(?,?,?,?,?,?,?);");
            enunciado.setInt(1, modeloPersona.getIdPersona());
            enunciado.setString(2, modeloPersona.getNombres());
            enunciado.setString(3, modeloPersona.getApellidos());
            enunciado.setString(4, modeloPersona.getTelefono());
            enunciado.setDouble(5, modeloPersona.getSalario());
            enunciado.setString(6, String.valueOf(modeloPersona.getRol()));
            enunciado.setInt(7, modeloPersona.getIdUsuario());
            enunciado.execute();
            cargarTablaModelos();
        } catch (SQLException e) {
            System.out.println("Error al editar una persona");
            e.printStackTrace();
        }
        
    }
    public void eliminarModelo(){
        modeloPersona = tablaPersona.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarPersona(?);");
            enunciado.setInt(1, modeloPersona.getIdPersona());
            enunciado.execute();
            cargarTablaModelos();
        } catch (SQLException e) {
            System.out.println("Error al eliminar una Persona");
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
        cbxUsuarios.getSelectionModel().clearSelection(); 

    }
    private void actualizarEstadoFormulario(EstadoFormulario estado){
        estadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.EDITAR);
        txtNombres.setDisable(!activo);
        txtApellidos.setDisable(!activo);
        txtTelefono.setDisable(!activo);
        spSalario.setDisable(!activo);
        cbxUsuarios.setDisable(!activo);
        
        tablaPersona.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);
        
        btnAgregar.setText(activo ? "Guardar":"Nuevo");
        btnEliminar.setText(activo ? "Cancelar":"Eliminar");
        btnActualizar.setDisable(activo);
        
    }
    
    @FXML
    private void btnVolverAction(){
      int indice = tablaPersona.getSelectionModel().getSelectedIndex();
      if(indice > 0){
          tablaPersona.getSelectionModel().select(indice-1);
      }
      cargarPersonaFormulario();
    }
    
    @FXML
    private void btnSiguienteAction(){
      int indice = tablaPersona.getSelectionModel().getSelectedIndex();
      if(indice < listaPersonas.size()-1){
          tablaPersona.getSelectionModel().select(indice+1);
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
            if(m.getNombres().toLowerCase().contains(nombre)){
                resultadoBusqueda.add(m);
            }
        }
        tablaPersona.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if(!resultadoBusqueda.isEmpty()){
            tablaPersona.getSelectionModel().selectFirst();
        }
    }
    
}
