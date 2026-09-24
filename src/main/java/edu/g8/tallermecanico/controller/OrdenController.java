package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class OrdenController {

    @FXML private TextField txtPlaca;
    @FXML private TextArea txtDiagnostico;
    @FXML private TableView<?> tablaOrdenes;

    @FXML
    public void onCrearOrden(ActionEvent event) {
        if (txtPlaca.getText().trim().isEmpty() || txtDiagnostico.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos Incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Debes ingresar la placa del vehículo y el diagnóstico inicial.");
            alert.showAndWait();
            return;
        }

        // Lógica para guardar la orden...

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Orden Creada");
        alert.setHeaderText(null);
        alert.setContentText("La orden de servicio fue registrada correctamente.");
        alert.showAndWait();

        onLimpiar(event);
    }

    @FXML
    public void onLimpiar(ActionEvent event) {
        if (txtPlaca != null) txtPlaca.clear();
        if (txtDiagnostico != null) txtDiagnostico.clear();
    }

    @FXML
    public void onVolverMenu(ActionEvent event) {
        SceneManager.cambiarVista("/view/MenuView.fxml", "Gestión General - Taller Mecánico");
    }
}
