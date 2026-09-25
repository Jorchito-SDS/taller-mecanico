package main.java.edu.g8.tallermecanico.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.g8.tallermecanico.config.ConnectionDb;
import main.java.edu.g8.tallermecanico.model.Repuesto;
import main.java.edu.g8.tallermecanico.service.RepuestoService;
import main.java.edu.g8.tallermecanico.util.SceneManager;
import main.java.edu.g8.tallermecanico.util.SesionUsuario;

public class RepuestoController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtStock;
    @FXML private TextField txtStockMinimo;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtProveedor;

    @FXML private TextField txtBuscar;

    @FXML private TableView<Repuesto> tblRepuestos;
    @FXML private TableColumn<Repuesto, String> colId;
    @FXML private TableColumn<Repuesto, String> colNombre;
    @FXML private TableColumn<Repuesto, Integer> colStock;
    @FXML private TableColumn<Repuesto, Integer> colStockMinimo;
    @FXML private TableColumn<Repuesto, Double> colPrecio;
    @FXML private TableColumn<Repuesto, String> colProveedor;

    private final RepuestoService repuestoService;
    private final SceneManager sceneManager;
    private final SesionUsuario sesion;

    private ObservableList<Repuesto> listaRepuestos;
    private Repuesto seleccionActual;

    public RepuestoController(RepuestoService repuestoService, SceneManager sceneManager, SesionUsuario sesion) {
        this.repuestoService = repuestoService;
        this.sceneManager = sceneManager;
        this.sesion = sesion;
    }

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

        tblRepuestos.getSelectionModel().selectedItemProperty().addListener((obs, ant, actual) -> {
            seleccionActual = actual;
            if (actual != null) {
                cargarEnFormulario(actual);
            }
        });

        if (txtBuscar != null) {
            txtBuscar.textProperty().addListener((obs, viejo, nuevo) -> handleBuscar());
        }

        handleListarTodos();
    }

    private void cargarEnFormulario(Repuesto r) {
        txtNombre.setText(r.getNombre());
        txtStock.setText(String.valueOf(r.getStock()));
        txtStockMinimo.setText(String.valueOf(r.getStockMinimo()));
        txtPrecio.setText(String.valueOf(r.getPrecio()));
        txtProveedor.setText(r.getProveedor());
    }

    @FXML
    private void handleRegistrar() {
        try (Connection conn = ConnectionDb.getConnection()) {
            Repuesto r = leerFormulario();
            if (repuestoService.registrarRepuesto(r, conn)) {
                sceneManager.showInfoAlert("Éxito", null, "Repuesto registrado correctamente.", AlertType.INFORMATION);
                limpiarCampos();
                handleListarTodos();
            }
        } catch (Exception e) {
            sceneManager.showInfoAlert("Error", null, e.getMessage() != null ? e.getMessage() : "No se pudo registrar el repuesto.", AlertType.ERROR);
        }
    }

    @FXML
    private void handleActualizar() {
        if (seleccionActual == null) {
            sceneManager.showInfoAlert("Selecciona un repuesto", null, "Elige un repuesto de la tabla para actualizarlo.", AlertType.WARNING);
            return;
        }
        try (Connection conn = ConnectionDb.getConnection()) {
            Repuesto r = leerFormulario();
            r.setIdRepuesto(seleccionActual.getIdRepuesto());
            if (repuestoService.actualizarRepuesto(r, conn)) {
                sceneManager.showInfoAlert("Éxito", null, "Repuesto actualizado correctamente.", AlertType.INFORMATION);
                limpiarCampos();
                handleListarTodos();
            }
        } catch (Exception e) {
            sceneManager.showInfoAlert("Error", null, e.getMessage() != null ? e.getMessage() : "No se pudo actualizar el repuesto.", AlertType.ERROR);
        }
    }

    @FXML
    private void handleEliminar() {
        if (seleccionActual == null) {
            sceneManager.showInfoAlert("Selecciona un repuesto", null, "Elige un repuesto de la tabla para eliminarlo.", AlertType.WARNING);
            return;
        }
        boolean confirmado = sceneManager.showConfirmAlert("Eliminar repuesto", null,
                "¿Seguro que deseas eliminar \"" + seleccionActual.getNombre() + "\"?");
        if (!confirmado) {
            return;
        }
        try (Connection conn = ConnectionDb.getConnection()) {
            if (repuestoService.eliminarRepuesto(seleccionActual.getIdRepuesto(), conn)) {
                limpiarCampos();
                handleListarTodos();
            }
        } catch (Exception e) {
            sceneManager.showInfoAlert("Error", null, "No se pudo eliminar el repuesto: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleListarTodos() {
        try (Connection conn = ConnectionDb.getConnection()) {
            listaRepuestos.setAll(repuestoService.listarTodos(conn));
        } catch (Exception e) {
            sceneManager.showInfoAlert("Error", null, "No se pudo obtener el listado de repuestos: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleBuscar() {
        try (Connection conn = ConnectionDb.getConnection()) {
            String filtro = txtBuscar != null && txtBuscar.getText() != null ? txtBuscar.getText().trim() : "";
            listaRepuestos.setAll(repuestoService.buscarPorNombre(filtro, conn));
        } catch (Exception e) {
            sceneManager.showInfoAlert("Error", null, "No se pudo buscar repuestos: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleConsultarStockBajo() {
        try (Connection conn = ConnectionDb.getConnection()) {
            listaRepuestos.setAll(repuestoService.listarRepuestosStockBajo(conn));
        } catch (Exception e) {
            sceneManager.showInfoAlert("Error", null, "No se pudo obtener el stock bajo: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void onLimpiar() {
        limpiarCampos();
    }

    @FXML
    private void onVolverMenu() throws Exception {
        sceneManager.showMenuGerenteView(sesion);
    }

    @FXML
    private void onCerrarSesion() throws Exception {
        sceneManager.showLoginView();
    }

    private Repuesto leerFormulario() {
        Repuesto r = new Repuesto();
        r.setNombre(txtNombre.getText().trim());
        r.setStock(Integer.parseInt(txtStock.getText().trim()));
        r.setStockMinimo(Integer.parseInt(txtStockMinimo.getText().trim()));
        r.setPrecio(Double.parseDouble(txtPrecio.getText().trim()));
        r.setProveedor(txtProveedor.getText().trim());
        return r;
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtStock.clear();
        txtStockMinimo.clear();
        txtPrecio.clear();
        txtProveedor.clear();
        seleccionActual = null;
        tblRepuestos.getSelectionModel().clearSelection();
    }
}
