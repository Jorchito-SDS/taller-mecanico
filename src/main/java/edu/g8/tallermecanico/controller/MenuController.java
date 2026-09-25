package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.java.edu.g8.tallermecanico.util.SceneManager;
import main.java.edu.g8.tallermecanico.util.SesionUsuario;

public class MenuController {

    @FXML private Label lblBienvenida;

    private final SceneManager sceneManager;
    private final SesionUsuario sesion;

    public MenuController(SceneManager sceneManager, SesionUsuario sesion) {
        this.sceneManager = sceneManager;
        this.sesion = sesion;
    }

    @FXML
    public void initialize() {
        if (lblBienvenida != null) {
            lblBienvenida.setText("Panel general — sesión de " + sesion.getNombre() + " activa");
        }
    }

    @FXML
    public void irAClientes(ActionEvent event) throws Exception {
        sceneManager.showClientesView(sesion);
    }

    @FXML
    public void irAVehiculos(ActionEvent event) throws Exception {
        sceneManager.showVehiculosView(sesion);
    }

    @FXML
    public void irAMecanicos(ActionEvent event) throws Exception {
        sceneManager.showMecanicosView(sesion);
    }

    @FXML
    public void irAOrdenes(ActionEvent event) throws Exception {
        sceneManager.showOrdenesView(sesion);
    }

    @FXML
    public void irAAsignarMecanico(ActionEvent event) throws Exception {
        sceneManager.showAsignarMecanicoView(sesion);
    }

    @FXML
    public void irARepuestos(ActionEvent event) throws Exception {
        sceneManager.showRepuestosView(sesion);
    }

    @FXML
    public void onCerrarSesion(ActionEvent event) throws Exception {
        sceneManager.showLoginView();
    }
}
