package main.java.edu.g8.tallermecanico.controller;

import java.sql.SQLException;
import java.util.List;
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
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.g8.tallermecanico.model.Mecanico;
import main.java.edu.g8.tallermecanico.repository.MecanicoRepository;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class MecanicoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtEspecialidad;
    @FXML private TextField txtTelefono;
    @FXML private CheckBox chkDisponible;
    @FXML private PasswordField txtPassword;

    @FXML private TableView<Mecanico> tablaMecanicos;
    @FXML private TableColumn<Mecanico, String> colNombre;
    @FXML private TableColumn<Mecanico, String> colEspecialidad;
    @FXML private TableColumn<Mecanico, String> colTelefono;

    private final MecanicoRepository mecanicoRepository = new MecanicoRepository();
    private final ObservableList<Mecanico> listaMecanicos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        cargarMecanicos();
    }

    private void cargarMecanicos() {
        try {
            listaMecanicos.clear();
            List<Mecanico> datos = mecanicoRepository.listarMecanicos();
            listaMecanicos.addAll(datos);
            tablaMecanicos.setItems(listaMecanicos);
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Conexión", "No se pudieron cargar los mecánicos desde la base de datos.");
        }
    }

    @FXML
    public void onGuardar(ActionEvent event) {
        if (txtNombre.getText().trim().isEmpty() || 
            txtEspecialidad.getText().trim().isEmpty() || 
            txtTelefono.getText().trim().isEmpty() ||
            txtPassword.getText().trim().isEmpty()) {
            
            mostrarAlerta(Alert.AlertType.WARNING, "Campos Incompletos", "Por favor completa todos los campos del formulario.");
            return;
        }

        try {
            Mecanico nuevoMecanico = new Mecanico(
                txtNombre.getText().trim(),
                txtEspecialidad.getText().trim(),
                txtTelefono.getText().trim(),
                chkDisponible.isSelected() ? 1 : 0,
                txtPassword.getText().trim()
            );

            if (mecanicoRepository.guardarMecanico(nuevoMecanico)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Mecánico guardado con éxito.");
                cargarMecanicos();
                onLimpiar(event);
            }
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "Ocurrió un error al intentar registrar el mecánico.");
        }
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
        SceneManager.cambiarVista("/view/MenuView.fxml", "Gestión General - Taller Mecánico");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}