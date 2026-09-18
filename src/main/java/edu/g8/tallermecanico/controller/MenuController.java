package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import main.java.edu.g8.tallermecanico.util.SceneManager;

public class MenuController {

    @FXML
    public void irAMecanicos(ActionEvent event) {
        SceneManager.cambiarVista("/resources/view/MecanicoView.fxml", "Gestión de Mecánicos");
    }

    @FXML
    public void irAOrdenes(ActionEvent event) {
        SceneManager.cambiarVista("/resources/view/OrdenView.fxml", "Órdenes de Servicio");
    }

    @FXML
    public void irAAsignarMecanico(ActionEvent event) {
        SceneManager.cambiarVista("/resources/view/AsignarMecanicoView.fxml", "Asignar Mecánico");
    }

    @FXML
    public void irAOrdenesPorMecanico(ActionEvent event) {
        SceneManager.cambiarVista("/resources/view/OrdenesPorMecanicoView.fxml", "Órdenes por Mecánico");
    }

  
}