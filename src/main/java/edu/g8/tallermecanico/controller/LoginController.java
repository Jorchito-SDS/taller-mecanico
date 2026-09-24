package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class LoginController {

    @FXML
    public void ingresarComoCliente(ActionEvent event) {
        SceneManager.cambiarVista("/view/ClienteView.fxml", "Portal del Cliente - Taller Mecánico");
    }

    @FXML
    public void ingresarComoMecanico(ActionEvent event) {
        SceneManager.cambiarVista("/view/MecanicoView.fxml", "Panel del Mecánico - Taller Mecánico");
    }

    @FXML
    public void ingresarComoGerente(ActionEvent event) {
        SceneManager.cambiarVista("/view/MenuView.fxml", "Gestión General - Taller Mecánico");
    }
}