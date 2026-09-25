package main.java.edu.g8.tallermecanico.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
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

    @FXML private TableView<Mecanico> tablaMecanicos;
    @FXML private TableColumn<Mecanico, String> colNombre;
    @FXML private TableColumn<Mecanico, String> colEspecialidad;
    @FXML private TableColumn<Mecanico, String> colTelefono;

    private final MecanicoService mecanicoService;
    private final SceneManager sceneManager;
    private final SesionUsuario sesion;
    private final ObservableList<Mecanico> listaMecanicos = FXCollections.observableArrayList();

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

        cargarMecanicos();
    }

    private void cargarMecanicos() {
        listaMecanicos.setAll(mecanicoService.listarMecanicos());
        tablaMecanicos.setItems(listaMecanicos);
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
    public void onLimpiar(ActionEvent event) {
        txtNombre.clear();
        txtEspecialidad.clear();
        txtTelefono.clear();
        txtPassword.clear();
        chkDisponible.setSelected(true);
    }

    @FXML
    public void onVolverMenu(ActionEvent event) throws Exception {
        sceneManager.showMenuGerenteView(sesion);
    }

    @FXML
    public void onCerrarSesion(ActionEvent event) throws Exception {
        sceneManager.showLoginView();
    }
}
