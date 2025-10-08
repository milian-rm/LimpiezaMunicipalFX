package org.gruponueve.controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

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
import org.gruponueve.model.Grupo;
import org.gruponueve.model.Persona;
import org.gruponueve.model.Rol;
import org.gruponueve.model.Orden;
import org.gruponueve.system.Main;

/**
 * FXML Controller class
 *
 * @author Roberto
 */
public class GrupoController implements Initializable {

    protected Main principal;
    private ObservableList<Grupo> listaGrupos;
    private Grupo modeloGrupo;
    private enum EstadoFormulario {AGREGAR, EDITAR, ELIMINAR, NINGUNA}
    EstadoFormulario estadoActual = EstadoFormulario.NINGUNA;

    @FXML
    protected TableView<Grupo> tablaGrupo;
    
    @FXML
    protected TableColumn colId, colIdOrden, colIdPersona;
    
    @FXML
    private TextField txtId, txtBuscar;
    
    @FXML
    private ComboBox<Orden> cbxOrdenes;
    
    @FXML
    private ComboBox<Persona> cbxPersonas;
    
    @FXML
    private Button btnBuscar, btnAnterior, btnSiguiente, btnAgregar, btnActualizar, btnEliminar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarOrden();
        cargarPersona();
        cargarTablaModelos();
        // expresiones lambda el metodo 
        tablaGrupo.setOnMouseClicked(eventHandler -> cargarGrupoFormulario());
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
        colId.setCellValueFactory(new PropertyValueFactory<Grupo, Integer>("idGrupo"));
        colIdOrden.setCellValueFactory(new PropertyValueFactory<Grupo, Integer>("idOrden"));
        colIdPersona.setCellValueFactory(new PropertyValueFactory<Grupo, Integer>("idPersona"));
      
    }
    
    private ArrayList<Grupo> listarGrupos(){
        ArrayList<Grupo> grupos = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarGrupo();");
            ResultSet resultado = enunciado.executeQuery();
            while(resultado.next()){
                grupos.add(new Grupo(
                            resultado.getInt("idGrupo"),
                            resultado.getInt("idOrden"),                            
                            resultado.getInt("idPersona")));
                        }
        } catch (SQLException ex) {
            System.out.println("Error al cargar de MySQL las Grupos");
            ex.printStackTrace();
        }
       return grupos;
    }
    private void cargarGrupoFormulario(){
        Grupo grupo = tablaGrupo.getSelectionModel().getSelectedItem();
        if (grupo != null) {
            txtId.setText(String.valueOf(grupo.getIdGrupoPersonas()));
            for (Orden o: cbxOrdenes.getItems()) {
                if (o.getIdOrden()== grupo.getIdOrden()) {
                    cbxOrdenes.setValue(o);
                    break;
                }
            }
            for (Persona p: cbxPersonas.getItems()) {
                if (p.getIdPersona()== grupo.getIdPersona()) {
                    cbxPersonas.setValue(p);
                    break;
                }
            }
        }
    }
    
   
    public void cargarTablaModelos(){
        listaGrupos = FXCollections.observableArrayList(listarGrupos());
        tablaGrupo.setItems(listaGrupos);
        tablaGrupo.getSelectionModel().selectFirst();
        cargarGrupoFormulario();
    }
    
    private Grupo cargarModeloGrupo(){
        int codigoGrupo = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());
        
        Orden ordenSeleccionada = cbxOrdenes.getSelectionModel().getSelectedItem();
        int codigoOrden = ordenSeleccionada != null ? ordenSeleccionada.getIdOrden(): 0;
        
        Persona personaSeleccionada = cbxPersonas.getSelectionModel().getSelectedItem();
        int codigoPersona = personaSeleccionada != null ? personaSeleccionada.getIdPersona(): 0;
        
        return new Grupo(
                codigoGrupo, 
                codigoOrden,
                codigoPersona);
    }
    
    private ArrayList<Orden> cargarModeloOrden(){
        ArrayList<Orden> ordenes = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_ListarOrdenes();");
            ResultSet resultado = enunciado.executeQuery();
            while(resultado.next()){
                Orden o = new Orden(
                        resultado.getInt(1), 
                        resultado.getString(2),
                        resultado.getString(3));
                ordenes.add(o);
            }
        } catch (SQLException a) {
            a.printStackTrace();
        }
        return ordenes;
    }
   
    private void cargarOrden(){
        ObservableList<Orden> ordenes = FXCollections.observableArrayList(cargarModeloOrden());
        cbxOrdenes.setItems(ordenes);
    }
    
    private ArrayList<Persona> cargarModeloPersona(){
        ArrayList<Persona> personas = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_ListarPersonas();");
            ResultSet resultado = enunciado.executeQuery();
            while(resultado.next()){
                Persona p = new Persona(
                        resultado.getInt(1), 
                        resultado.getString(2),
                        resultado.getString(3),
                        resultado.getString(4),
                        resultado.getDouble(5),
                        Rol.valueOf(resultado.getString(6)),
                        resultado.getInt(7));
                personas.add(p);
            }
        } catch (SQLException a) {
            a.printStackTrace();
        }
        return personas;
    }
    
    private void cargarPersona(){
        ObservableList<Persona> personas = FXCollections.observableArrayList(cargarModeloPersona());
        cbxPersonas.setItems(personas);
    }
    
    
    public void agregarModelo(){
        modeloGrupo = cargarModeloGrupo();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion().
                    prepareCall("call sp_AgregarGrupo(?,?);");
            enunciado.setInt(1, modeloGrupo.getIdOrden());
            enunciado.setInt(2, modeloGrupo.getIdPersona());
            int registrosAgregados = enunciado.executeUpdate();
            if(registrosAgregados > 0){
                System.out.println("Grupo agregada");
                cargarTablaModelos();
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar una Grupo");
            e.printStackTrace();
        }
        
    }
    
    public void actualizarModelo(){
        modeloGrupo = cargarModeloGrupo();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarGrupo(?,?,?);");
            enunciado.setInt(1, modeloGrupo.getIdGrupoPersonas());
            enunciado.setInt(2, modeloGrupo.getIdOrden());
            enunciado.setInt(3, modeloGrupo.getIdPersona());
            enunciado.execute();
            cargarTablaModelos();
        } catch (SQLException e) {
            System.out.println("Error al editar una grupo");
            e.printStackTrace();
        }
        
    }
    public void eliminarModelo(){
        modeloGrupo = tablaGrupo.getSelectionModel().getSelectedItem();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarGrupo(?);");
            enunciado.setInt(1, modeloGrupo.getIdGrupoPersonas());
            enunciado.execute();
            cargarTablaModelos();
        } catch (SQLException e) {
            System.out.println("Error al eliminar una Grupo");
            e.printStackTrace();
        }
    }
    private void limpiarCampos(){
        Double reinicio = 0.0;
        txtId.clear();
        cbxOrdenes.getSelectionModel().clearSelection(); 
        cbxPersonas.getSelectionModel().clearSelection(); 

    }
    private void actualizarEstadoFormulario(EstadoFormulario estado){
        estadoActual = estado;
        boolean activo = (estado == EstadoFormulario.AGREGAR || estado == EstadoFormulario.EDITAR);
        cbxOrdenes.setDisable(!activo);
        cbxPersonas.setDisable(!activo);
        
        tablaGrupo.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);
        
        btnAgregar.setText(activo ? "Guardar":"Nuevo");
        btnEliminar.setText(activo ? "Cancelar":"Eliminar");
        btnActualizar.setDisable(activo);
        
    }
    
    @FXML
    private void btnVolverAction(){
      int indice = tablaGrupo.getSelectionModel().getSelectedIndex();
      if(indice > 0){
          tablaGrupo.getSelectionModel().select(indice-1);
      }
      cargarGrupoFormulario();
    }
    
    @FXML
    private void btnSiguienteAction(){
      int indice = tablaGrupo.getSelectionModel().getSelectedIndex();
      if(indice < listaGrupos.size()-1){
          tablaGrupo.getSelectionModel().select(indice+1);
      }
      cargarGrupoFormulario();
    }
    
    @FXML
    private void agregarGrupo(){
        switch (estadoActual) {
            case NINGUNA:
                limpiarCampos();
                System.out.println("Voy a crear un registro para grupos");
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
    private void editarGrupo(){
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
        ArrayList<Grupo> resultadoBusqueda = new ArrayList<>();
        for(Grupo m:listaGrupos){
            if(m.getNombres().toLowerCase().contains(nombre)){
                resultadoBusqueda.add(m);
            }
        }
        tablaGrupo.setItems(FXCollections.observableArrayList(resultadoBusqueda));
        if(!resultadoBusqueda.isEmpty()){
            tablaGrupo.getSelectionModel().selectFirst();
        }
    }
    
    
}
