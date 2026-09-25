
package main.java.edu.g8.tallermecanico.controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.g8.tallermecanico.model.Vehiculo;
import main.java.edu.g8.tallermecanico.service.VehiculoService;


public class VehiculoController implements Initializable {

    private final VehiculoService vehiculoService = new VehiculoService();

    @FXML private TextField txtPlaca;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtAnio;
    @FXML private TextField txtKilometraje;
    @FXML private TextField txtIdCliente;
    @FXML private TextField txtBuscar;

    @FXML private TableView<Vehiculo> tablaVehiculos;
    @FXML private TableColumn<Vehiculo, String> colPlaca;
    @FXML private TableColumn<Vehiculo, String> colMarca;
    @FXML private TableColumn<Vehiculo, String> colModelo;
    @FXML private TableColumn<Vehiculo, Integer> colAnio;
    @FXML private TableColumn<Vehiculo, Integer> colKilometraje;
    @FXML private TableColumn<Vehiculo, Integer> colCliente;

    private ObservableList<Vehiculo> listaVehiculos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colKilometraje.setCellValueFactory(new PropertyValueFactory<>("kilometraje"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("id_cliente"));

        cargarTabla();
    }

    private void cargarTabla() {
        listaVehiculos = FXCollections.observableArrayList(vehiculoService.listarVehiculos());
        tablaVehiculos.setItems(listaVehiculos);
    }

  @FXML
public void guardarVehiculo() {
    try {
        String idCliente = txtIdCliente.getText(); 
        String placa = txtPlaca.getText();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        int anio = Integer.parseInt(txtAnio.getText());

        Vehiculo vehiculo = new Vehiculo(idCliente, marca, modelo, anio, placa);

        if (vehiculoService.registrarVehiculo(vehiculo)) {
            cargarTabla();
            limpiarCampos();
        }
    } catch (NumberFormatException e) {
        System.out.println("Error de formato: Verifica los campos numéricos.");
    }
    }

    private void limpiarCampos() {
        txtPlaca.clear();
        txtMarca.clear();
        txtModelo.clear();
        txtAnio.clear();
        txtKilometraje.clear();
        txtIdCliente.clear();
    }
}