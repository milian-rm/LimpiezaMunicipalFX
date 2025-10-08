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
import org.gruponueve.model.Usuario;
import org.gruponueve.system.Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class UsuarioController implements Initializable {

    private Main principal;
    private ObservableList<Usuario> listaUsuario;
    private Usuario modeloUsuario;

    private enum EstadoFormulario {
        AGREGAR, EDITAR, ELIMINAR, NINGUNO
    };
    EstadoFormulario estadoActual = EstadoFormulario.NINGUNO;

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private TableColumn colID, colCorreo, colContra;

    @FXML
    private TextField txtBuscar, txtId, txtCorreo, txtContra;

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
        tablaUsuarios.setOnMouseClicked(event -> cargarEnTextoField());
        txtBuscar.setOnAction(eh -> BuscarTabla());
    }


    public void configurarColumna() {
        colID.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("idUsuario"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<Usuario, String>("correo"));
        colContra.setCellValueFactory(new PropertyValueFactory<Usuario, String>("contrasenia"));
    }

    public void cargarTabla() {
        listaUsuario = FXCollections.observableList(ListarTabla());
        tablaUsuarios.setItems(listaUsuario);
        tablaUsuarios.getSelectionModel().selectFirst();
        if (tablaUsuarios.getSelectionModel().getSelectedItem() != null) {
            cargarEnTextoField();
        }
    }

    public void cargarEnTextoField() {
        Usuario usuarioSeleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        txtId.setText(String.valueOf(usuarioSeleccionado.getIdUsuario()));
        txtCorreo.setText(usuarioSeleccionado.getCorreo());
        txtContra.setText(usuarioSeleccionado.getContrasenia());
    }

    public ArrayList<Usuario> ListarTabla() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ListarUsuarios();");
            ResultSet resultado = enunciado.executeQuery();

            while (resultado.next()) {
                usuarios.add(new Usuario(
                        resultado.getInt("idUsuario"),
                        resultado.getString("correo"),
                        resultado.getString("contrasenia")));
            }
        } catch (SQLException ex) {
            System.out.println("Error al cargar de MySQL: " + ex.getMessage());
            ex.printStackTrace();
        }
        return usuarios;
    }

    public Usuario obtenerModeloUsuario() {
        int idUsuario = txtId.getText().isEmpty() ? 0 : Integer.parseInt(txtId.getText());
        String correo = txtCorreo.getText();
        String contrasenia = txtContra.getText();
        return new Usuario(idUsuario, correo, contrasenia);
    }


    public void agregarUsuario() {
        modeloUsuario = obtenerModeloUsuario();
        System.out.println("Agregando Usuario: " + modeloUsuario);
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_AgregarUsuario(?,?);");
            enunciado.setString(1, modeloUsuario.getCorreo());
            enunciado.setString(2, modeloUsuario.getContrasenia());
            enunciado.executeQuery();
            cargarTabla();
        } catch (SQLException e) {
            System.out.println("Error al agregar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void actualizarUsuario() {
        modeloUsuario = obtenerModeloUsuario();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_ActualizarUsuario(?,?,?);");
            enunciado.setInt(1, modeloUsuario.getIdUsuario());
            enunciado.setString(2, modeloUsuario.getCorreo());
            enunciado.setString(3, modeloUsuario.getContrasenia());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarUsuario() {
        modeloUsuario = obtenerModeloUsuario();
        try {
            CallableStatement enunciado = Conexion.getInstancia().getConexion()
                    .prepareCall("call sp_EliminarUsuario(?);");
            enunciado.setInt(1, modeloUsuario.getIdUsuario());
            enunciado.executeQuery();
            cargarTabla();
        } catch (Exception e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }

    public void limpiarFormulario() {
        txtId.clear();
        txtCorreo.clear();
        txtContra.clear();
    }

    public void estadoFormulario(EstadoFormulario est) {
        estadoActual = est;
        boolean activo = (est == EstadoFormulario.AGREGAR || est == EstadoFormulario.EDITAR);

        txtCorreo.setDisable(!activo);
        txtContra.setDisable(!activo);
        tablaUsuarios.setDisable(activo);
        btnBuscar.setDisable(activo);
        txtBuscar.setDisable(activo);

        //btnVolver.setDisable(activo);
        btnAnterior.setDisable(activo);
        btnSiguiente.setDisable(activo);
        btnEditar.setDisable(activo);
        btnNuevo.setText(activo ? "Guardar": "Agregar");
        btnEliminar.setText(activo ? "Cancelar":"Eliminar");
    }

    @FXML
    private void btnAnteriorAction() {
        int indice = tablaUsuarios.getSelectionModel().getSelectedIndex();
        if (indice > 0) {
            tablaUsuarios.getSelectionModel().select(indice - 1);
            cargarEnTextoField();
        }
    }

    @FXML
    private void btnSiguienteAction() {
        int indice = tablaUsuarios.getSelectionModel().getSelectedIndex();
        if (indice < listaUsuario.size() - 1) {
            tablaUsuarios.getSelectionModel().select(indice + 1);
            cargarEnTextoField();
        }
    }

    @FXML
    private void nuevoUsuario(){
        switch (estadoActual) {
            case NINGUNO:
                limpiarFormulario();
                estadoFormulario(EstadoFormulario.AGREGAR);
                break;
            case AGREGAR:
                agregarUsuario();
                estadoFormulario(EstadoFormulario.NINGUNO);
                break;
            case EDITAR:
                actualizarUsuario();
                estadoFormulario(EstadoFormulario.NINGUNO);
                break;
        }
    }
    
    @FXML
    private void editarUsuario(){
        estadoFormulario(EstadoFormulario.EDITAR);
    }
    @FXML
    private void cancelarEliminar(){
        if(estadoActual == EstadoFormulario.NINGUNO){
            eliminarUsuario();
        }else{
            estadoFormulario(EstadoFormulario.NINGUNO);
        }
    }

    
    @FXML
    private void BuscarTabla() {
        String texto = txtBuscar.getText().toLowerCase();
        ArrayList<Usuario> resultadoBusqueda = new ArrayList<>();
        for (Usuario usu : listaUsuario) {
            if (String.valueOf(usu.getCorreo()).contains(texto) ||
                usu.getCorreo().toLowerCase().contains(texto)) {
                resultadoBusqueda.add(usu);
            }
        }
        tablaUsuarios.setItems(FXCollections.observableList(resultadoBusqueda));
        if (!resultadoBusqueda.isEmpty()) {
            tablaUsuarios.getSelectionModel().selectFirst();
        }
    }
}
