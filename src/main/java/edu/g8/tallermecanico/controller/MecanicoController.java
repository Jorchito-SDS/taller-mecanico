package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import main.java.edu.g8.tallermecanico.model.Mecanico;
import main.java.edu.g8.tallermecanico.service.MecanicoService;

public class MecanicoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtEspecialidad;
    @FXML private TextField txtTelefono;
    @FXML private CheckBox chkDisponible;
    @FXML private javafx.scene.control.PasswordField txtPassword;

    @FXML private TableView<Mecanico> tablaMecanicos;
    @FXML private TableColumn<Mecanico, String> colNombre;
    @FXML private TableColumn<Mecanico, String> colEspecialidad;
    @FXML private TableColumn<Mecanico, String> colTelefono;

    private final MecanicoService mecanicoService = new MecanicoService();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));
        colEspecialidad.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getEspecialidad()));
        colTelefono.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));

        cargarTabla();
    }

    private void cargarTabla() {
        ObservableList<Mecanico> lista = FXCollections.observableArrayList(
                mecanicoService.listarMecanicos());
        tablaMecanicos.setItems(lista);
    }

    @FXML
    public void onGuardar(ActionEvent event) {
        try {
            Mecanico nuevo = new Mecanico(
                    null,
                    txtNombre.getText(),
                    txtEspecialidad.getText(),
                    txtTelefono.getText(),
                    chkDisponible.isSelected() ? 1 : 0,
                    null // la contraseña se asigna en un flujo aparte (creación de usuario)
            );

            boolean exito = mecanicoService.registrarMecanico(nuevo, txtPassword.getText());

            if (exito) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Mecánico registrado correctamente.");
                limpiarFormulario();
                cargarTabla();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "No se pudo guardar el mecánico.");
            }

        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.WARNING, e.getMessage());
        }
    }

    @FXML
    public void onLimpiar(ActionEvent event) {
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtEspecialidad.clear();
        txtTelefono.clear();
        txtPassword.clear();
        chkDisponible.setSelected(true);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle("Gestión de Mecánicos");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}