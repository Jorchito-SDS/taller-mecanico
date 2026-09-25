package main.java.edu.g8.tallermecanico.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.g8.tallermecanico.model.Vehiculo;
import main.java.edu.g8.tallermecanico.service.VehiculoService;
import main.java.edu.g8.tallermecanico.util.SceneManager;
import main.java.edu.g8.tallermecanico.util.SesionUsuario;

/**
 * CRUD completo de vehículos, disponible para el perfil Gerente desde el menú
 * general.
 */
public class VehiculoController implements Initializable {

    @FXML private TextField txtPlaca;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtAnio;
    @FXML private TextField txtIdCliente;
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
    @FXML private TableColumn<Vehiculo, String> colCliente;

    private final VehiculoService vehiculoService;
    private final SceneManager sceneManager;
    private final SesionUsuario sesion;

    private ObservableList<Vehiculo> listaVehiculos;
    private Vehiculo seleccionActual;

    public VehiculoController(VehiculoService vehiculoService, SceneManager sceneManager, SesionUsuario sesion) {
        this.vehiculoService = vehiculoService;
        this.sceneManager = sceneManager;
        this.sesion = sesion;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("idCliente"));

        tablaVehiculos.getSelectionModel().selectedItemProperty().addListener((obs, anterior, actual) -> {
            seleccionActual = actual;
            if (actual != null) {
                cargarEnFormulario(actual);
            }
        });

        cargarTabla();
        actualizarEstadoBotones();
    }

    private void cargarTabla() {
        listaVehiculos = FXCollections.observableArrayList(vehiculoService.listarVehiculos());
        tablaVehiculos.setItems(listaVehiculos);
    }

    private void cargarEnFormulario(Vehiculo v) {
        txtPlaca.setText(v.getPlaca());
        txtMarca.setText(v.getMarca());
        txtModelo.setText(v.getModelo());
        txtAnio.setText(String.valueOf(v.getAnio()));
        txtIdCliente.setText(v.getIdCliente());
        actualizarEstadoBotones();
    }

    @FXML
    public void guardarVehiculo() {
        Vehiculo vehiculo = leerFormulario();
        if (vehiculo == null) {
            return;
        }
        if (vehiculoService.registrarVehiculo(vehiculo)) {
            cargarTabla();
            limpiarCampos();
            sceneManager.showInfoAlert("Vehículo registrado", null, "El vehículo se guardó correctamente.", AlertType.INFORMATION);
        } else {
            sceneManager.showInfoAlert("No se pudo registrar", null, "Verifica que la placa no esté ya registrada.", AlertType.ERROR);
        }
    }

    @FXML
    public void onActualizar() {
        if (seleccionActual == null) {
            sceneManager.showInfoAlert("Selecciona un vehículo", null, "Elige un vehículo de la tabla para actualizarlo.", AlertType.WARNING);
            return;
        }
        Vehiculo vehiculo = leerFormulario();
        if (vehiculo == null) {
            return;
        }
        vehiculo.setIdVehiculo(seleccionActual.getIdVehiculo());
        if (vehiculoService.actualizarVehiculo(vehiculo)) {
            cargarTabla();
            limpiarCampos();
            sceneManager.showInfoAlert("Vehículo actualizado", null, "Los cambios se guardaron correctamente.", AlertType.INFORMATION);
        } else {
            sceneManager.showInfoAlert("No se pudo actualizar", null, "Ocurrió un error al actualizar el vehículo.", AlertType.ERROR);
        }
    }

    @FXML
    public void onEliminar() {
        if (seleccionActual == null) {
            sceneManager.showInfoAlert("Selecciona un vehículo", null, "Elige un vehículo de la tabla para eliminarlo.", AlertType.WARNING);
            return;
        }
        boolean confirmado = sceneManager.showConfirmAlert("Eliminar vehículo", null,
                "¿Seguro que deseas eliminar el vehículo con placa " + seleccionActual.getPlaca() + "?");
        if (!confirmado) {
            return;
        }
        if (vehiculoService.eliminarVehiculo(seleccionActual.getIdVehiculo())) {
            cargarTabla();
            limpiarCampos();
        } else {
            sceneManager.showInfoAlert("No se pudo eliminar", null, "Ocurrió un error al eliminar el vehículo.", AlertType.ERROR);
        }
    }

    @FXML
    public void onBuscar() {
        String placa = txtBuscar.getText() == null ? "" : txtBuscar.getText().trim();
        if (placa.isEmpty()) {
            cargarTabla();
            return;
        }
        Vehiculo encontrado = vehiculoService.buscarVehiculoPorPlaca(placa);
        listaVehiculos.setAll(encontrado != null ? java.util.List.of(encontrado) : java.util.List.of());
    }

    @FXML
    public void onLimpiar() {
        limpiarCampos();
    }

    @FXML
    public void onVolverMenu() throws Exception {
        sceneManager.showMenuGerenteView(sesion);
    }

    @FXML
    public void onCerrarSesion() throws Exception {
        sceneManager.showLoginView();
    }

    private Vehiculo leerFormulario() {
        try {
            String idCliente = txtIdCliente.getText().trim();
            String placa = txtPlaca.getText().trim();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());

            if (idCliente.isEmpty() || placa.isEmpty() || marca.isEmpty() || modelo.isEmpty()) {
                sceneManager.showInfoAlert("Campos incompletos", null, "Completa placa, marca, modelo y cliente.", AlertType.WARNING);
                return null;
            }
            return new Vehiculo(idCliente, marca, modelo, anio, placa);
        } catch (NumberFormatException e) {
            sceneManager.showInfoAlert("Año inválido", null, "Ingresa un año numérico válido.", AlertType.WARNING);
            return null;
        }
    }

    private void limpiarCampos() {
        txtPlaca.clear();
        txtMarca.clear();
        txtModelo.clear();
        txtAnio.clear();
        txtIdCliente.clear();
        seleccionActual = null;
        tablaVehiculos.getSelectionModel().clearSelection();
        actualizarEstadoBotones();
    }

    private void actualizarEstadoBotones() {
        boolean haySeleccion = seleccionActual != null;
        if (btnActualizar != null) btnActualizar.setDisable(!haySeleccion);
        if (btnEliminar != null) btnEliminar.setDisable(!haySeleccion);
    }
}