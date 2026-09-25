package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import main.java.edu.g8.tallermecanico.util.Rol;
import main.java.edu.g8.tallermecanico.util.SceneManager;

/**
 * Pantalla inicial: el usuario elige con qué perfil quiere entrar. Cada botón
 * lleva a la MISMA pantalla de login (RolLoginView), pero configurada para
 * ese perfil, de modo que cliente, mecánico y gerente siempre pasan por un
 * inicio de sesión antes de ver su panel.
 */
public class LoginController {

    private final SceneManager sceneManager;

    public LoginController(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    public void ingresarComoCliente(ActionEvent event) {
        abrirLoginDe(Rol.CLIENTE);
    }

    @FXML
    public void ingresarComoMecanico(ActionEvent event) {
        abrirLoginDe(Rol.MECANICO);
    }

    @FXML
    public void ingresarComoGerente(ActionEvent event) {
        abrirLoginDe(Rol.GERENTE);
    }

    private void abrirLoginDe(Rol rol) {
        try {
            sceneManager.showRolLoginView(rol);
        } catch (Exception e) {
            sceneManager.showInfoAlert("No se pudo abrir la pantalla", "Error", e.getMessage(), AlertType.ERROR);
        }
    }
}
