package main.java.edu.g8.tallermecanico.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.g8.tallermecanico.model.Mecanico;
import main.java.edu.g8.tallermecanico.service.MecanicoService;
import main.java.edu.g8.tallermecanico.util.SceneManager;
import main.java.edu.g8.tallermecanico.util.SesionUsuario;

public class MecanicoController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtEspecialidad;
    @FXML private TextField txtTelefono;
    @FXML private CheckBox chkDisponible;
    @FXML private PasswordField txtPassword;

    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;

    @FXML private TableView<Mecanico> tablaMecanicos;
    @FXML private TableColumn<Mecanico, String> colNombre;
    @FXML private TableColumn<Mecanico, String> colEspecialidad;
    @FXML private TableColumn<Mecanico, String> colTelefono;

    private final MecanicoService mecanicoService;
    private final SceneManager sceneManager;
    private final SesionUsuario sesion;
    private final ObservableList<Mecanico> listaMecanicos = FXCollections.observableArrayList();
    private Mecanico seleccionActual;

    public MecanicoController(MecanicoService mecanicoService, SceneManager sceneManager, SesionUsuario sesion) {
        this.mecanicoService = mecanicoService;
        this.sceneManager = sceneManager;
        this.sesion = sesion;
    }

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));

        tablaMecanicos.getSelectionModel().selectedItemProperty().addListener((obs, ant, actual) -> {
            seleccionActual = actual;
            if (actual != null) {
                cargarEnFormulario(actual);
            }
            actualizarEstadoBotones();
        });

        cargarMecanicos();
        actualizarEstadoBotones();
    }

    private void cargarMecanicos() {
        listaMecanicos.setAll(mecanicoService.listarMecanicos());
        tablaMecanicos.setItems(listaMecanicos);
    }

    private void cargarEnFormulario(Mecanico m) {
        txtNombre.setText(m.getNombre());
        txtEspecialidad.setText(m.getEspecialidad());
        txtTelefono.setText(m.getTelefono());
        chkDisponible.setSelected(m.getDisponible() == 1);
        if (txtPassword != null) txtPassword.clear();
    }

    @FXML
    public void onGuardar(ActionEvent event) {
        if (txtNombre.getText().trim().isEmpty() ||
            txtEspecialidad.getText().trim().isEmpty() ||
            txtTelefono.getText().trim().isEmpty() ||
            txtPassword.getText().trim().isEmpty()) {

            sceneManager.showInfoAlert("Campos Incompletos", null, "Por favor completa todos los campos del formulario.", AlertType.WARNING);
            return;
        }

        Mecanico nuevoMecanico = new Mecanico(
            txtNombre.getText().trim(),
            txtEspecialidad.getText().trim(),
            txtTelefono.getText().trim(),
            chkDisponible.isSelected() ? 1 : 0,
            null
        );

        if (mecanicoService.registrarMecanico(nuevoMecanico, txtPassword.getText().trim())) {
            sceneManager.showInfoAlert("Éxito", null, "Mecánico guardado con éxito. Ya puede iniciar sesión con su nombre y contraseña.", AlertType.INFORMATION);
            cargarMecanicos();
            onLimpiar(event);
        } else {
            sceneManager.showInfoAlert("Error al Guardar", null, "Ocurrió un error al intentar registrar el mecánico.", AlertType.ERROR);
        }
    }

    @FXML
    public void onActualizar(ActionEvent event) {
        if (seleccionActual == null) {
            sceneManager.showInfoAlert("Selecciona un mecánico", null, "Elige un mecánico de la tabla para actualizarlo.", AlertType.WARNING);
            return;
        }
        if (txtNombre.getText().trim().isEmpty() || txtEspecialidad.getText().trim().isEmpty()
                || txtTelefono.getText().trim().isEmpty()) {
            sceneManager.showInfoAlert("Campos Incompletos", null, "Nombre, especialidad y teléfono son obligatorios.", AlertType.WARNING);
            return;
        }

        Mecanico m = new Mecanico(
            seleccionActual.getIdMecanico(),
            txtNombre.getText().trim(),
            txtEspecialidad.getText().trim(),
            txtTelefono.getText().trim(),
            chkDisponible.isSelected() ? 1 : 0
        );

        try {
            String nuevaPassword = txtPassword.getText() != null ? txtPassword.getText().trim() : "";
            if (mecanicoService.actualizarMecanico(m, nuevaPassword)) {
                sceneManager.showInfoAlert("Éxito", null, "Mecánico actualizado correctamente.", AlertType.INFORMATION);
                cargarMecanicos();
                onLimpiar(event);
            } else {
                sceneManager.showInfoAlert("Error al Actualizar", null, "Ocurrió un error al actualizar el mecánico.", AlertType.ERROR);
            }
        } catch (Exception e) {
            sceneManager.showInfoAlert("Error", null, "No se pudo actualizar: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void onEliminar(ActionEvent event) {
        if (seleccionActual == null) {
            sceneManager.showInfoAlert("Selecciona un mecánico", null, "Elige un mecánico de la tabla para eliminarlo.", AlertType.WARNING);
            return;
        }
        boolean confirmado = sceneManager.showConfirmAlert("Eliminar mecánico", null,
                "¿Seguro que deseas eliminar a \"" + seleccionActual.getNombre()
                        + "\"? Sus órdenes asignadas quedarán sin mecánico.");
        if (!confirmado) return;

        try {
            if (mecanicoService.eliminarMecanico(seleccionActual.getIdMecanico())) {
                cargarMecanicos();
                onLimpiar(event);
            } else {
                sceneManager.showInfoAlert("Error al Eliminar", null, "Ocurrió un error al eliminar el mecánico.", AlertType.ERROR);
            }
        } catch (Exception e) {
            sceneManager.showInfoAlert("Error", null, "No se pudo eliminar: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void onLimpiar(ActionEvent event) {
        txtNombre.clear();
        txtEspecialidad.clear();
        txtTelefono.clear();
        txtPassword.clear();
        chkDisponible.setSelected(true);
        seleccionActual = null;
        tablaMecanicos.getSelectionModel().clearSelection();
        actualizarEstadoBotones();
    }

    @FXML
    public void onVolverMenu(ActionEvent event) throws Exception {
        sceneManager.showMenuGerenteView(sesion);
    }

    @FXML
    public void onCerrarSesion(ActionEvent event) throws Exception {
        sceneManager.showLoginView();
    }

    private void actualizarEstadoBotones() {
        boolean haySeleccion = seleccionActual != null;
        if (btnActualizar != null) btnActualizar.setDisable(!haySeleccion);
        if (btnEliminar != null) btnEliminar.setDisable(!haySeleccion);
    }
}
