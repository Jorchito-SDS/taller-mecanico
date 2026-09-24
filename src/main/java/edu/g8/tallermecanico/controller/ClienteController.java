package main.java.edu.g8.tallermecanico.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.g8.tallermecanico.model.Vehiculo;
import main.java.edu.g8.tallermecanico.service.VehiculoService;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class ClienteController implements Initializable {

    private final VehiculoService vehiculoService = new VehiculoService();

    @FXML private TableView<Vehiculo> tablaMisVehiculos;
    @FXML private TableColumn<Vehiculo, String> colPlaca;
    @FXML private TableColumn<Vehiculo, String> colMarca;
    @FXML private TableColumn<Vehiculo, String> colModelo;
    @FXML private TableColumn<Vehiculo, Integer> colAnio;
    @FXML private TableColumn<Vehiculo, Integer> colKilometraje;

    @FXML private TextField txtPlaca;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtAnio;
    @FXML private TextField txtKilometraje;

    @FXML private TextField txtBuscarPlaca;
    @FXML private Label lblEstadoActual;
    @FXML private Label lblDetalleOrden;

    @FXML private TextField txtPlacaCita;
    @FXML private DatePicker dpFechaCita;
    @FXML private TextField txtMotivoCita;
    @FXML private Label lblMensajeCita;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colKilometraje.setCellValueFactory(new PropertyValueFactory<>("kilometraje"));

        cargarVehiculos();
    }

    private void cargarVehiculos() {
        ObservableList<Vehiculo> lista = FXCollections.observableArrayList(vehiculoService.listarVehiculos());
        tablaMisVehiculos.setItems(lista);
    }

   @FXML
public void onGuardarVehiculo(ActionEvent event) {
    try {
        String placa = txtPlaca.getText();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        int anio = Integer.parseInt(txtAnio.getText());

        // ID de cliente temporal como String ("1")
        String idCliente = "1"; 

        // Instanciar Vehiculo con los 5 parámetros requeridos
        Vehiculo v = new Vehiculo(idCliente, marca, modelo, anio, placa);

        if (vehiculoService.registrarVehiculo(v)) {
            cargarVehiculos();
            txtPlaca.clear();
            txtMarca.clear();
            txtModelo.clear();
            txtAnio.clear();
        }
    } catch (NumberFormatException e) {
        System.out.println("Error: Ingrese valores numéricos válidos en el año.");
    }
    }

    @FXML
    public void onBuscarEstadoOrden(ActionEvent event) {
        String placa = txtBuscarPlaca.getText();
        if (placa == null || placa.trim().isEmpty()) {
            lblEstadoActual.setText("Estado: Ingrese una placa válida");
            return;
        }
        // Simulación de búsqueda de estado de reparación
        lblEstadoActual.setText("Estado: EN REPARACIÓN");
        lblDetalleOrden.setText("Vehículo " + placa + " se encuentra actualmente en cambio de repuestos y afinamiento.");
    }

    @FXML
    public void onAgendarCita(ActionEvent event) {
        if (dpFechaCita.getValue() == null || txtPlacaCita.getText().isEmpty()) {
            lblMensajeCita.setText("Por favor complete la fecha y la placa.");
            return;
        }
        lblMensajeCita.setText("Cita registrada exitosamente para el " + dpFechaCita.getValue().toString());
    }

    @FXML
    public void onVolverMenu(ActionEvent event) {
        SceneManager.cambiarVista("/resources/view/LoginView.fxml", "Acceso - Taller Mecánico");
    }
}