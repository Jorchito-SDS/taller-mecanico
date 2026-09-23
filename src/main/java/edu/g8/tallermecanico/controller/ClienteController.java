/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.g8.tallermecanico.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.java.edu.g8.tallermecanico.model.Cliente;
import main.java.edu.g8.tallermecanico.service.ClienteService;

/**
 *
 * @author informatica
 */
public class ClienteController {
    
    @FXML private TextField txtFiltro;
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, String> colDireccion;

    private final ClienteService clienteService = new ClienteService();

    @FXML
    public void initialize() {
        
        colNombre.setCellValueFactory(data -> 
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colTelefono.setCellValueFactory(data -> 
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));
        colEmail.setCellValueFactory(data -> 
                new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colDireccion.setCellValueFactory(data -> 
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDireccion()));

        cargarTabla("");
    }

    @FXML
    public void onBuscar(ActionEvent event) {
        String filtro = txtFiltro.getText();
        cargarTabla(filtro);
    }

    private void cargarTabla(String filtro) {
        ObservableList<Cliente> lista = FXCollections.observableArrayList(
                clienteService.buscarClientes(filtro)
        );
        tablaClientes.setItems(lista);
    }
}