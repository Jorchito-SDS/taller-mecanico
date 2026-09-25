package main.java.edu.g8.tallermecanico.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.g8.tallermecanico.model.Orden;
import main.java.edu.g8.tallermecanico.service.OrdenService;
import main.java.edu.g8.tallermecanico.util.SceneManager;
import main.java.edu.g8.tallermecanico.util.SesionUsuario;

public class OrdenController implements Initializable {

    @FXML private TextField txtPlaca;
    @FXML private TextArea txtDiagnostico;

    @FXML private TableView<Orden> tablaOrdenes;
    @FXML private TableColumn<Orden, String> colPlaca;
    @FXML private TableColumn<Orden, String> colCliente;
    @FXML private TableColumn<Orden, String> colMecanico;
    @FXML private TableColumn<Orden, String> colEstado;

    private final OrdenService ordenService;
    private final SceneManager sceneManager;
    private final SesionUsuario sesion;

    private ObservableList<Orden> listaOrdenes;
    private Orden seleccionActual;

    public OrdenController(OrdenService ordenService, SceneManager sceneManager, SesionUsuario sesion) {
        this.ordenService = ordenService;
        this.sceneManager = sceneManager;
        this.sesion = sesion;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colMecanico.setCellValueFactory(new PropertyValueFactory<>("mecanico"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaOrdenes.getSelectionModel().selectedItemProperty().addListener((obs, ant, actual) -> {
            seleccionActual = actual;
        });

        cargarOrdenes();
    }

    private void cargarOrdenes() {
        listaOrdenes = FXCollections.observableArrayList(ordenService.listarOrdenes());
        tablaOrdenes.setItems(listaOrdenes);
    }

    @FXML
    public void onCrearOrden(ActionEvent event) {
        String placa = txtPlaca.getText() == null ? "" : txtPlaca.getText().trim();
        String diagnostico = txtDiagnostico.getText() == null ? "" : txtDiagnostico.getText().trim();

        if (placa.isEmpty() || diagnostico.isEmpty()) {
            sceneManager.showInfoAlert("Campos Incompletos", null, "Debes ingresar la placa del vehículo y el diagnóstico inicial.", AlertType.WARNING);
            return;
        }

        try {
            if (ordenService.crearOrdenPorPlaca(placa, diagnostico)) {
                sceneManager.showInfoAlert("Orden Creada", null, "La orden de servicio fue registrada correctamente.", AlertType.INFORMATION);
                cargarOrdenes();
                onLimpiar(event);
            } else {
                sceneManager.showInfoAlert("Vehículo no encontrado", null,
                        "No existe ningún vehículo registrado con la placa " + placa + ".", AlertType.ERROR);
            }
        } catch (Exception e) {
            sceneManager.showInfoAlert("Error al Crear Orden", null, "Ocurrió un error al registrar la orden.", AlertType.ERROR);
        }
    }

    @FXML
    public void onEliminar(ActionEvent event) {
        if (seleccionActual == null) {
            sceneManager.showInfoAlert("Selecciona una orden", null,
                    "Elige una orden de la tabla para eliminarla.", AlertType.WARNING);
            return;
        }
        boolean confirmado = sceneManager.showConfirmAlert("Eliminar orden", null,
                "¿Seguro que deseas eliminar la orden de la placa \"" + seleccionActual.getPlaca() + "\"?");
        if (!confirmado) {
            return;
        }
        if (ordenService.eliminarOrden(seleccionActual.getIdOrden())) {
            seleccionActual = null;
            cargarOrdenes();
        } else {
            sceneManager.showInfoAlert("No se pudo eliminar", null,
                    "Ocurrió un error al eliminar la orden.", AlertType.ERROR);
        }
    }

    @FXML
    public void onLimpiar(ActionEvent event) {
        if (txtPlaca != null) txtPlaca.clear();
        if (txtDiagnostico != null) txtDiagnostico.clear();
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
