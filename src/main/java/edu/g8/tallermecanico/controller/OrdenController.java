package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import main.java.edu.g8.tallermecanico.model.Orden;
import main.java.edu.g8.tallermecanico.service.OrdenService;

public class OrdenController {

    @FXML private TextField txtPlaca; // el recepcionista ingresa la placa, no un ID interno
    @FXML private TextArea txtDiagnostico;
    @FXML private Label lblMensaje;

    @FXML private TableView<Orden> tablaOrdenes;
    @FXML private TableColumn<Orden, String> colPlaca;
    @FXML private TableColumn<Orden, String> colCliente;
    @FXML private TableColumn<Orden, String> colMecanico;
    @FXML private TableColumn<Orden, String> colEstado;

    private final OrdenService ordenService = new OrdenService();

    @FXML
    public void initialize() {
        colPlaca.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPlacaVehiculo()));
        colCliente.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreCliente()));
        colMecanico.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreMecanico()));
        colEstado.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));

        cargarTabla();
    }

    private void cargarTabla() {
        ObservableList<Orden> lista = FXCollections.observableArrayList(
                ordenService.listarActivas());
        tablaOrdenes.setItems(lista);
    }

    @FXML
    public void onCrearOrden(ActionEvent event) {
        try {
            boolean exito = ordenService.crearOrdenPorPlaca(
                    txtPlaca.getText(),
                    txtDiagnostico.getText()
            );

            if (exito) {
                lblMensaje.setText("Orden creada correctamente.");
                limpiarFormulario();
                cargarTabla();
            } else {
                lblMensaje.setText("No se pudo crear la orden. Intente de nuevo.");
            }

        } catch (IllegalArgumentException e) {
            mostrarAlerta(e.getMessage());
        }
    }

    @FXML
    public void onLimpiar(ActionEvent event) {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtPlaca.clear();
        txtDiagnostico.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Orden de Servicio");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}