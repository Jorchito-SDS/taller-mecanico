package main.java.edu.g8.tallermecanico.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class MecanicoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtEspecialidad;
    @FXML private TextField txtTelefono;
    @FXML private CheckBox chkDisponible;
    @FXML private PasswordField txtPassword;

    @FXML private TableView<?> tablaMecanicos;
    @FXML private TableColumn<?, String> colNombre;
    @FXML private TableColumn<?, String> colEspecialidad;
    @FXML private TableColumn<?, String> colTelefono;

    @FXML
    public void onGuardar(ActionEvent event) {
        // Validar que los campos obligatorios no estén vacíos
        if (txtNombre.getText().trim().isEmpty() || 
            txtEspecialidad.getText().trim().isEmpty() || 
            txtTelefono.getText().trim().isEmpty()) {
            
            // Alerta de error directa en el controlador
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Validación");
            alert.setHeaderText(null);
            alert.setContentText("Por favor completa los campos obligatorios: Nombre, Especialidad y Teléfono.");
            alert.showAndWait();
            return;
        }

        // --- Lógica para guardar o insertar en lista/base de datos ---

        // Alerta de confirmación de éxito directa en el controlador
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("El mecánico ha sido registrado correctamente.");
        alert.showAndWait();

        onLimpiar(event);
    }

    @FXML
    public void onLimpiar(ActionEvent event) {
        txtNombre.clear();
        txtEspecialidad.clear();
        txtTelefono.clear();
        txtPassword.clear();
        chkDisponible.setSelected(true);
    }

    @FXML
    public void onVolverMenu(ActionEvent event) {
        SceneManager.cambiarVista("/view/LoginView.fxml", "Acceso - Taller Mecánico");
    }
}