package main.java.edu.g8.tallermecanico.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.g8.tallermecanico.model.Vehiculo;
import main.java.edu.g8.tallermecanico.service.VehiculoService;
import main.java.edu.g8.tallermecanico.util.SceneManager;
import main.java.edu.g8.tallermecanico.util.SesionUsuario;

public class VehiculoController implements Initializable {

    @FXML private TextField txtPlaca;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtAnio;
    @FXML private TextField txtKilometraje;
    @FXML private TextField txtIdCliente;
    @FXML private TextField txtEstado;
    @FXML private TextField txtBuscar;

    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;

    @FXML private TableView<Vehiculo> tablaVehiculos;
    @FXML private TableColumn<Vehiculo, String> colPlaca;
    @FXML private TableColumn<Vehiculo, String> colMarca;
    @FXML private TableColumn<Vehiculo, String> colModelo;
    @FXML private TableColumn<Vehiculo, Integer> colAnio;
    @FXML private TableColumn<Vehiculo, Integer> colKilometraje;
    @FXML private TableColumn<Vehiculo, String> colCliente;
    @FXML private TableColumn<Vehiculo, String> colEstado;

    private VehiculoService vehiculoService;
    private SceneManager sceneManager;
    private SesionUsuario sesion;

    private ObservableList<Vehiculo> listaVehiculos;
    private Vehiculo seleccionActual;

    // Constructor sin parámetros (requerido por JavaFX / FXMLLoader)
    public VehiculoController() {
        this.vehiculoService = new VehiculoService();
    }

    // Constructor parametrizado (requerido por SceneManager)
    public VehiculoController(VehiculoService vehiculoService, SceneManager sceneManager, SesionUsuario sesion) {
        this.vehiculoService = vehiculoService;
        this.sceneManager = sceneManager;
        this.sesion = sesion;
    }

    // Invocado automáticamente por SceneManager.cargarVista() vía reflexión,
    // ya que este controlador se instancia con el constructor vacío.
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.sesion = sceneManager.getSesion();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (colPlaca != null) colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        if (colMarca != null) colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        if (colModelo != null) colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        if (colAnio != null) colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        if (colKilometraje != null) colKilometraje.setCellValueFactory(new PropertyValueFactory<>("kilometraje"));
        if (colCliente != null) colCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
        if (colEstado != null) colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        listaVehiculos = FXCollections.observableArrayList();
        if (tablaVehiculos != null) {
            tablaVehiculos.setItems(listaVehiculos);
            tablaVehiculos.getSelectionModel().selectedItemProperty().addListener((obs, ant, actual) -> {
                seleccionActual = actual;
                if (actual != null) {
                    cargarEnFormulario(actual);
                }
            });
        }

        if (txtBuscar != null) {
            txtBuscar.textProperty().addListener((obs, viejo, nuevo) -> onBuscar(null));
        }

        cargarTabla();
    }

    private void cargarTabla() {
        if (vehiculoService == null || listaVehiculos == null) return;
        listaVehiculos.setAll(vehiculoService.listarVehiculos());
    }

    private void cargarEnFormulario(Vehiculo v) {
        if (txtPlaca != null) txtPlaca.setText(v.getPlaca());
        if (txtMarca != null) txtMarca.setText(v.getMarca());
        if (txtModelo != null) txtModelo.setText(v.getModelo());
        if (txtAnio != null) txtAnio.setText(String.valueOf(v.getAnio()));
        if (txtKilometraje != null) txtKilometraje.setText(String.valueOf(v.getKilometraje()));
        if (txtIdCliente != null) txtIdCliente.setText(v.getIdCliente());
        if (txtEstado != null) txtEstado.setText(v.getEstado());
    }

    @FXML
    private void guardarVehiculo(ActionEvent event) {
        Vehiculo v = leerFormulario();
        if (v == null) return;

        if (vehiculoService.registrarVehiculo(v)) {
            sceneManager.showInfoAlert("Éxito", null, "Vehículo registrado correctamente.", AlertType.INFORMATION);
            onLimpiar(event);
            cargarTabla();
        } else {
            sceneManager.showInfoAlert("No se pudo registrar", null,
                    "Ya existe un vehículo con esa placa o los datos son inválidos.", AlertType.ERROR);
        }
    }

    @FXML
    private void onActualizar(ActionEvent event) {
        if (seleccionActual == null) {
            sceneManager.showInfoAlert("Selecciona un vehículo", null,
                    "Elige un vehículo de la tabla para actualizarlo.", AlertType.WARNING);
            return;
        }
        Vehiculo v = leerFormulario();
        if (v == null) return;
        v.setIdVehiculo(seleccionActual.getIdVehiculo());

        if (vehiculoService.actualizarVehiculo(v)) {
            sceneManager.showInfoAlert("Éxito", null, "Vehículo actualizado correctamente.", AlertType.INFORMATION);
            onLimpiar(event);
            cargarTabla();
        } else {
            sceneManager.showInfoAlert("No se pudo actualizar", null,
                    "Ocurrió un error al actualizar el vehículo.", AlertType.ERROR);
        }
    }

    @FXML
    private void onEliminar(ActionEvent event) {
        if (seleccionActual == null) {
            sceneManager.showInfoAlert("Selecciona un vehículo", null,
                    "Elige un vehículo de la tabla para eliminarlo.", AlertType.WARNING);
            return;
        }
        boolean confirmado = sceneManager.showConfirmAlert("Eliminar vehículo", null,
                "¿Seguro que deseas eliminar el vehículo con placa \"" + seleccionActual.getPlaca() + "\"?");
        if (!confirmado) return;

        if (vehiculoService.eliminarVehiculo(seleccionActual.getIdVehiculo())) {
            onLimpiar(event);
            cargarTabla();
        } else {
            sceneManager.showInfoAlert("No se pudo eliminar", null,
                    "Ocurrió un error al eliminar el vehículo.", AlertType.ERROR);
        }
    }

    @FXML
    private void onBuscar(ActionEvent event) {
        String filtro = txtBuscar != null && txtBuscar.getText() != null ? txtBuscar.getText().trim() : "";
        listaVehiculos.setAll(vehiculoService.buscarPorPlacaLike(filtro));
    }

    @FXML
    private void onLimpiar(ActionEvent event) {
        if (txtPlaca != null) txtPlaca.clear();
        if (txtMarca != null) txtMarca.clear();
        if (txtModelo != null) txtModelo.clear();
        if (txtAnio != null) txtAnio.clear();
        if (txtKilometraje != null) txtKilometraje.clear();
        if (txtIdCliente != null) txtIdCliente.clear();
        if (txtEstado != null) txtEstado.clear();
        seleccionActual = null;
        if (tablaVehiculos != null) tablaVehiculos.getSelectionModel().clearSelection();
    }

    private Vehiculo leerFormulario() {
        String placa = txtPlaca != null && txtPlaca.getText() != null ? txtPlaca.getText().trim() : "";
        String marca = txtMarca != null && txtMarca.getText() != null ? txtMarca.getText().trim() : "";
        String modelo = txtModelo != null && txtModelo.getText() != null ? txtModelo.getText().trim() : "";
        String anioTxt = txtAnio != null && txtAnio.getText() != null ? txtAnio.getText().trim() : "";
        String kmTxt = txtKilometraje != null && txtKilometraje.getText() != null ? txtKilometraje.getText().trim() : "";
        String idCliente = txtIdCliente != null && txtIdCliente.getText() != null ? txtIdCliente.getText().trim() : "";
        String estado = txtEstado != null && txtEstado.getText() != null ? txtEstado.getText().trim() : "";

        if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || idCliente.isEmpty()) {
            sceneManager.showInfoAlert("Campos incompletos", null,
                    "Placa, marca, modelo y cliente ID son obligatorios.", AlertType.WARNING);
            return null;
        }

        int anio;
        int km;
        try {
            anio = anioTxt.isEmpty() ? 0 : Integer.parseInt(anioTxt);
            km = kmTxt.isEmpty() ? 0 : Integer.parseInt(kmTxt);
        } catch (NumberFormatException e) {
            sceneManager.showInfoAlert("Datos inválidos", null,
                    "Año y kilometraje deben ser números.", AlertType.WARNING);
            return null;
        }

        Vehiculo v = new Vehiculo();
        v.setPlaca(placa);
        v.setMarca(marca);
        v.setModelo(modelo);
        v.setAnio(anio);
        v.setKilometraje(km);
        v.setIdCliente(idCliente);
        v.setEstado(estado.isEmpty() ? "Activo" : estado);
        return v;
    }

    // --- Navegación del panel lateral ---

    @FXML
    private void irAInicio(ActionEvent event) throws Exception {
        sceneManager.showMenuGerenteView(sesion);
    }

    @FXML
    private void irAVehiculos(ActionEvent event) {
        cargarTabla();
    }

    @FXML
    private void irAClientes(ActionEvent event) throws Exception {
        sceneManager.showClientesView(sesion);
    }

    @FXML
    private void irAAjustes(ActionEvent event) throws Exception {
        sceneManager.showMenuGerenteView(sesion);
    }
}
