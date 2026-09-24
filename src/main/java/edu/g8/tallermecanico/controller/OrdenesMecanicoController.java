package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class OrdenesMecanicoController {

    @FXML private ComboBox<?> cmbMecanicos;
    @FXML private TableView<?> tablaOrdenesMecanico;

    @FXML
    public void onVerOrdenes(ActionEvent event) {
        if (cmbMecanicos.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Filtro Requerido");
            alert.setHeaderText(null);
            alert.setContentText("Por favor selecciona un mecánico para consultar sus órdenes de trabajo.");
            alert.showAndWait();
            return;
        }

        // --- Lógica para filtrar y cargar las órdenes del mecánico en tablaOrdenesMecanico ---
    }

    @FXML
    public void onVolverMenu(ActionEvent event) {
        SceneManager.cambiarVista("/view/MenuView.fxml", "Gestión General - Taller Mecánico");
    }
}