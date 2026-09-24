package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class OrdenesMecanicoController {

    @FXML private ComboBox<?> cmbMecanicos;
    @FXML private TableView<?> tablaOrdenesMecanico;

    @FXML
    public void onVerOrdenes(ActionEvent event) {
        // Lógica para filtrar órdenes por mecánico
    }

    @FXML
    public void onVolverMenu(ActionEvent event) {
        SceneManager.cambiarVista("/view/MenuView.fxml", "Gestión General - Taller Mecánico");
    }
}