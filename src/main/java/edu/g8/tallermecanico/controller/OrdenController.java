package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        // Lógica para crear orden de servicio
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