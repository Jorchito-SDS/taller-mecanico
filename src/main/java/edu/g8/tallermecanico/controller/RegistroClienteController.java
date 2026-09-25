package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.UUID;
import main.java.edu.g8.tallermecanico.model.Cliente;
import main.java.edu.g8.tallermecanico.service.ClienteService;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class RegistroClienteController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtDireccion;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmarPassword;

    private SceneManager sceneManager;
    private final ClienteService clienteService;

    public RegistroClienteController() {
        this.clienteService = new ClienteService();
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    public void onRegistrar(ActionEvent event) {
        String nombre = txtNombre != null && txtNombre.getText() != null ? txtNombre.getText().trim() : "";
        String telefono = txtTelefono != null && txtTelefono.getText() != null ? txtTelefono.getText().trim() : "";
        String email = txtEmail != null && txtEmail.getText() != null ? txtEmail.getText().trim() : "";
        String direccion = txtDireccion != null && txtDireccion.getText() != null ? txtDireccion.getText().trim() : "";
        String password = txtPassword != null && txtPassword.getText() != null ? txtPassword.getText() : "";
        String confirmar = txtConfirmarPassword != null && txtConfirmarPassword.getText() != null ? txtConfirmarPassword.getText() : "";

        if (nombre.isEmpty() || email.isEmpty()) {
            if (sceneManager != null) {
                sceneManager.showInfoAlert("Campos Incompletos", null, "Por favor ingresa nombre y correo electrónico.", AlertType.WARNING);
            }
            return;
        }

        if (!password.isEmpty() && !password.equals(confirmar)) {
            if (sceneManager != null) {
                sceneManager.showInfoAlert("Contraseñas no coinciden", null, "La contraseña y la confirmación no coinciden.", AlertType.WARNING);
            }
            return;
        }

        try {
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setIdCliente(UUID.randomUUID().toString());
            nuevoCliente.setNombre(nombre);
            nuevoCliente.setTelefono(telefono.isEmpty() ? "00000000" : telefono);
            nuevoCliente.setEmail(email);
            nuevoCliente.setDireccion(direccion);

            boolean registrado = password.isEmpty()
                    ? clienteService.registrarCliente(nuevoCliente)
                    : clienteService.registrarCliente(nuevoCliente, password);

            if (registrado) {
                if (sceneManager != null) {
                    sceneManager.showInfoAlert("Registro Exitoso", null, "¡Cliente registrado correctamente!", AlertType.INFORMATION);
                    sceneManager.showLoginView();
                }
            } else {
                if (sceneManager != null) {
                    sceneManager.showInfoAlert("Error de Registro", null, "No se pudo realizar el registro en la base de datos.", AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (sceneManager != null) {
                sceneManager.showInfoAlert("Error de Base de Datos", "Detalle del error", e.getMessage(), AlertType.ERROR);
            }
        }
    }

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