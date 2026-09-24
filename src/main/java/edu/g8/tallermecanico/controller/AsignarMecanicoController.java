package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class AsignarMecanicoController {

    @FXML private ComboBox<?> cmbOrdenes;
    @FXML private ComboBox<?> cmbMecanicos;

    @FXML
    public void onAsignar(ActionEvent event) {
        if (cmbOrdenes.getValue() == null || cmbMecanicos.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selección Incompleta");
            alert.setHeaderText(null);
            alert.setContentText("Debes seleccionar una orden y un mecánico para realizar la asignación.");
            alert.showAndWait();
            return;
        }

        // --- Lógica para guardar la asignación en la Base de Datos ---

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Asignación Exitosa");
        alert.setHeaderText(null);
        alert.setContentText("El mecánico ha sido asignado correctamente a la orden seleccionada.");
        alert.showAndWait();
    }

    @FXML
    public void onVolverMenu(ActionEvent event) {
        SceneManager.cambiarVista("/view/MenuView.fxml", "Gestión General - Taller Mecánico");
    }
}