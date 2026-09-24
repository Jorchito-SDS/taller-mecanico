package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class AsignarMecanicoController {

    @FXML private ComboBox<?> cmbOrdenes;
    @FXML private ComboBox<?> cmbMecanicos;

    @FXML
    public void onAsignar(ActionEvent event) {
        // Lógica para asignar mecánico
    }

    @FXML
    public void onVolverMenu(ActionEvent event) {
        SceneManager.cambiarVista("/view/MenuView.fxml", "Gestión General - Taller Mecánico");
    }
}