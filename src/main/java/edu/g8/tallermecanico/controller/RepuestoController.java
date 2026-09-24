package main.java.edu.g8.tallermecanico.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.g8.tallermecanico.config.ConnectionDb;
import main.java.edu.g8.tallermecanico.model.Repuesto;
import main.java.edu.g8.tallermecanico.service.RepuestoService;

public class RepuestoController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtStock;
    @FXML private TextField txtStockMinimo;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtProveedor;

    @FXML private TableView<Repuesto> tblRepuestos;
    @FXML private TableColumn<Repuesto, Integer> colId;
    @FXML private TableColumn<Repuesto, String> colNombre;
    @FXML private TableColumn<Repuesto, Integer> colStock;
    @FXML private TableColumn<Repuesto, Integer> colStockMinimo;
    @FXML private TableColumn<Repuesto, Double> colPrecio;
    @FXML private TableColumn<Repuesto, String> colProveedor;

    private final RepuestoService repuestoService = new RepuestoService();
    private ObservableList<Repuesto> listaRepuestos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("idRepuesto"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colStockMinimo.setCellValueFactory(new PropertyValueFactory<>("stockMinimo"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));

        listaRepuestos = FXCollections.observableArrayList();
        tblRepuestos.setItems(listaRepuestos);
    }

    @FXML
    private void handleRegistrar() {
        try (Connection conn = ConnectionDb.getConnection()) {
            Repuesto r = new Repuesto();
            r.setNombre(txtNombre.getText());
            r.setStock(Integer.parseInt(txtStock.getText()));
            r.setStockMinimo(Integer.parseInt(txtStockMinimo.getText()));
            r.setPrecio(Double.parseDouble(txtPrecio.getText()));
            r.setProveedor(txtProveedor.getText());

            if (repuestoService.registrarRepuesto(r, conn)) {
                mostrarAlerta("Éxito", "Repuesto registrado correctamente.", Alert.AlertType.INFORMATION);
                limpiarCampos();
            }
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleConsultarStockBajo() {
        try (Connection conn = ConnectionDb.getConnection()) {
            List<Repuesto> stockBajo = repuestoService.listarRepuestosStockBajo(conn);
            listaRepuestos.setAll(stockBajo);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo obtener el stock bajo: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtStock.clear();
        txtStockMinimo.clear();
        txtPrecio.clear();
        txtProveedor.clear();
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}