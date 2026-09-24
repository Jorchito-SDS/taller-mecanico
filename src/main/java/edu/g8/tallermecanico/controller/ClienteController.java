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
import main.java.edu.g8.tallermecanico.util.UserSession;

public class ClienteController implements Initializable {

    private final VehiculoService vehiculoService = new VehiculoService();

    @FXML private TableView<Vehiculo> tablaVehiculos;
    @FXML private TableColumn<Vehiculo, String> colPlaca;
    @FXML private TableColumn<Vehiculo, String> colMarca;
    @FXML private TableColumn<Vehiculo, String> colModelo;
    @FXML private TableColumn<Vehiculo, Integer> colAnio;

    @FXML private TextField txtPlaca;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtAnio;

    @FXML private TextField txtPlacaCita;
    @FXML private DatePicker dpFechaCita;
    @FXML private TextArea txtMotivo;
    @FXML private Label lblMensajeCita;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));

        cargarVehiculos();
    }

    private void cargarVehiculos() {
        ObservableList<Vehiculo> lista = FXCollections.observableArrayList(vehiculoService.listarVehiculos());
        if (tablaVehiculos != null) {
            tablaVehiculos.setItems(lista);
        }
    }

    @FXML
    public void onRegistrarVehiculo(ActionEvent event) {
        try {
            String placa = txtPlaca.getText();
            String marca = txtMarca.getText();
            String modelo = txtModelo.getText();
            int anio = Integer.parseInt(txtAnio.getText());

            String idCliente = "1"; 
            Vehiculo v = new Vehiculo(idCliente, marca, modelo, anio, placa);

            if (vehiculoService.registrarVehiculo(v)) {
                cargarVehiculos();
                txtPlaca.clear();
                txtMarca.clear();
                txtModelo.clear();
                txtAnio.clear();
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un año válido.");
        }
    }

    @FXML
    public void onAgendarCita(ActionEvent event) {
        if (dpFechaCita.getValue() == null || txtPlacaCita.getText() == null || txtPlacaCita.getText().trim().isEmpty()) {
            lblMensajeCita.setText("Por favor complete la fecha y la placa.");
            return;
        }
        lblMensajeCita.setText("Cita registrada exitosamente para el " + dpFechaCita.getValue().toString());
    }

@FXML
public void onCerrarSesion() {
    UserSession session = UserSession.getInstance();
    if (session != null) {
        session.limpiarSesion();
    }
    SceneManager.cambiarVista("/resources/view/LoginView.fxml", "Inicio de Sesión");
}
}