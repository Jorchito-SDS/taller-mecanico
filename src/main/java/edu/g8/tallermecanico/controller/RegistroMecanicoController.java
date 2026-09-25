package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.UUID;
import main.java.edu.g8.tallermecanico.model.Mecanico;
import main.java.edu.g8.tallermecanico.service.MecanicoService;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class RegistroMecanicoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtEspecialidad;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmarPassword;

    private SceneManager sceneManager;
    private final MecanicoService mecanicoService;

    public RegistroMecanicoController() {
        this.mecanicoService = new MecanicoService();
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    public void onRegistrar(ActionEvent event) {
        String nombre = txtNombre != null && txtNombre.getText() != null ? txtNombre.getText().trim() : "";
        String especialidad = txtEspecialidad != null && txtEspecialidad.getText() != null ? txtEspecialidad.getText().trim() : "";
        String password = txtPassword != null && txtPassword.getText() != null ? txtPassword.getText() : "";
        String confirmar = txtConfirmarPassword != null && txtConfirmarPassword.getText() != null ? txtConfirmarPassword.getText() : "";

        if (nombre.isEmpty() || password.isEmpty()) {
            if (sceneManager != null) {
                sceneManager.showInfoAlert("Campos Incompletos", null, "Ingresa tu nombre y contraseña.", AlertType.WARNING);
            }
            return;
        }

        if (!password.equals(confirmar)) {
            if (sceneManager != null) {
                sceneManager.showInfoAlert("Error de Contraseña", null, "Las contraseñas no coinciden.", AlertType.WARNING);
            }
            return;
        }

        try {
            Mecanico nuevoMecanico = new Mecanico();
            
            // Genera un ID por si la columna id_mecanico en BD no es AUTO_INCREMENT
            try {
                nuevoMecanico.setIdMecanico("MEC-" + UUID.randomUUID().toString().substring(0, 5));
            } catch (Exception ignored) {
            }

            nuevoMecanico.setNombre(nombre);
            nuevoMecanico.setEspecialidad(especialidad);

            boolean registrado = mecanicoService.registrarMecanico(nuevoMecanico, password);

            if (registrado) {
                if (sceneManager != null) {
                    sceneManager.showInfoAlert("Registro Exitoso", null, "Mecánico registrado correctamente.", AlertType.INFORMATION);
                    sceneManager.showLoginView();
                }
            } else {
                if (sceneManager != null) {
                    sceneManager.showInfoAlert("Error de Registro", null, "No se pudo registrar el mecánico en la base de datos.", AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (sceneManager != null) {
                sceneManager.showInfoAlert("Error de Registro", "Excepción", e.getMessage(), AlertType.ERROR);
            }
        }
    }

    // --- MÉTODOS DE NAVEGACIÓN (ALIAS PARA COMPATIBILIDAD CON EL FXML) ---

    @FXML
    public void onVolverLogin(ActionEvent event) {
        onVolver(event);
    }

    @FXML
    public void onVolverPerfil(ActionEvent event) {
        onVolver(event);
    }

    @FXML
    public void onVolver(ActionEvent event) {
        if (sceneManager != null) {
            sceneManager.showLoginView();
        }
    }
}