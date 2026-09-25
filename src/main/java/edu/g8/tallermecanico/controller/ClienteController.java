package main.java.edu.g8.tallermecanico.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.g8.tallermecanico.model.Orden;
import main.java.edu.g8.tallermecanico.model.Vehiculo;
import main.java.edu.g8.tallermecanico.service.OrdenService;
import main.java.edu.g8.tallermecanico.service.VehiculoService;
import main.java.edu.g8.tallermecanico.util.SceneManager;
import main.java.edu.g8.tallermecanico.util.SesionUsuario;

/**
 * Portal del cliente logueado: solo ve y gestiona sus propios vehículos y
 * órdenes, identificados por el idReferencia que trae la sesión recibida por
 * constructor.
 */
public class ClienteController implements Initializable {

    @FXML private Label lblBienvenida;

    @FXML private TableView<Vehiculo> tablaVehiculos;
    @FXML private TableColumn<Vehiculo, String> colPlaca;
    @FXML private TableColumn<Vehiculo, String> colMarca;
    @FXML private TableColumn<Vehiculo, String> colModelo;
    @FXML private TableColumn<Vehiculo, Integer> colAnio;

    @FXML private TextField txtPlaca;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtAnio;

    @FXML private TableView<Orden> tablaOrdenes;
    @FXML private TableColumn<Orden, String> colOrdenVehiculo;
    @FXML private TableColumn<Orden, String> colOrdenEstado;
    @FXML private TableColumn<Orden, String> colOrdenDiagnostico;

    @FXML private TextField txtPlacaCita;
    @FXML private DatePicker dpFechaCita;
    @FXML private TextArea txtMotivo;
    @FXML private Label lblMensajeCita;

    private final VehiculoService vehiculoService;
    private final OrdenService ordenService;
    private final SceneManager sceneManager;
    private final SesionUsuario sesion;

    public ClienteController(VehiculoService vehiculoService, OrdenService ordenService,
            SceneManager sceneManager, SesionUsuario sesion) {
        this.vehiculoService = vehiculoService;
        this.ordenService = ordenService;
        this.sceneManager = sceneManager;
        this.sesion = sesion;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));

        if (colOrdenVehiculo != null) {
            colOrdenVehiculo.setCellValueFactory(new PropertyValueFactory<>("placa"));
            colOrdenEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            colOrdenDiagnostico.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        }

        if (lblBienvenida != null && sesion != null) {
            lblBienvenida.setText("Hola, " + sesion.getNombre());
        }

        cargarVehiculos();
        cargarOrdenes();
    }

    private String idClienteSesion() {
        return sesion != null ? sesion.getIdReferencia() : null;
    }

    private void cargarVehiculos() {
        String idCliente = idClienteSesion();
        ObservableList<Vehiculo> lista = FXCollections.observableArrayList(
            idCliente != null ? vehiculoService.listarPorCliente(idCliente) : java.util.List.of()
        );
        if (tablaVehiculos != null) {
            tablaVehiculos.setItems(lista);
        }
    }

    private void cargarOrdenes() {
        String idCliente = idClienteSesion();
        ObservableList<Orden> lista = FXCollections.observableArrayList(
            idCliente != null ? ordenService.listarPorCliente(Integer.parseInt(idCliente)) : java.util.List.of()
        );
        if (tablaOrdenes != null) {
            tablaOrdenes.setItems(lista);
        }
    }

    @FXML
    public void onRegistrarVehiculo(ActionEvent event) {
        try {
            String placa = txtPlaca.getText().trim();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());
            String idCliente = idClienteSesion();

            if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || idCliente == null) {
                sceneManager.showInfoAlert("Campos incompletos", null, "Completa placa, marca y modelo.", AlertType.WARNING);
                return;
            }

            Vehiculo v = new Vehiculo(idCliente, marca, modelo, anio, placa);

            if (vehiculoService.registrarVehiculo(v)) {
                cargarVehiculos();
                txtPlaca.clear();
                txtMarca.clear();
                txtModelo.clear();
                txtAnio.clear();
            } else {
                sceneManager.showInfoAlert("No se pudo registrar", null, "Verifica que la placa no esté ya registrada.", AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            sceneManager.showInfoAlert("Año inválido", null, "Ingresa un año numérico válido.", AlertType.WARNING);
        }
    }

    @FXML
    public void onAgendarCita(ActionEvent event) {
        String placa = txtPlacaCita.getText() == null ? "" : txtPlacaCita.getText().trim();
        String motivo = txtMotivo.getText() == null ? "" : txtMotivo.getText().trim();

        if (dpFechaCita.getValue() == null || placa.isEmpty() || motivo.isEmpty()) {
            lblMensajeCita.getStyleClass().setAll("mensaje-error");
            lblMensajeCita.setText("Completa la placa, la fecha y el motivo de la visita.");
            return;
        }

        try {
            Vehiculo vehiculo = vehiculoService.buscarVehiculoPorPlaca(placa);
            String idCliente = idClienteSesion();
            if (vehiculo == null || idCliente == null || !idCliente.equals(vehiculo.getIdCliente())) {
                lblMensajeCita.getStyleClass().setAll("mensaje-error");
                lblMensajeCita.setText("Esa placa no corresponde a ninguno de tus vehículos registrados.");
                return;
            }
            boolean creada = ordenService.crearOrdenPorPlaca(placa, motivo, dpFechaCita.getValue());
            if (creada) {
                lblMensajeCita.getStyleClass().setAll("mensaje-ok");
                lblMensajeCita.setText("Cita registrada para el " + dpFechaCita.getValue() + ".");
                txtPlacaCita.clear();
                txtMotivo.clear();
                dpFechaCita.setValue(null);
                cargarOrdenes();
            } else {
                lblMensajeCita.getStyleClass().setAll("mensaje-error");
                lblMensajeCita.setText("No encontramos un vehículo tuyo con esa placa.");
            }
        } catch (Exception e) {
            lblMensajeCita.getStyleClass().setAll("mensaje-error");
            lblMensajeCita.setText("Ocurrió un error al registrar la cita.");
        }
    }

    @FXML
    public void onCerrarSesion() throws Exception {
        sceneManager.showLoginView();
    }
}
