package main.java.edu.g8.tallermecanico.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.g8.tallermecanico.model.Orden;
import main.java.edu.g8.tallermecanico.service.OrdenService;
import main.java.edu.g8.tallermecanico.util.SceneManager;
import main.java.edu.g8.tallermecanico.util.SesionUsuario;

/**
 * Panel del mecánico logueado: solo muestra las órdenes asignadas a él,
 * según el idReferencia que trae la sesión recibida por constructor.
 */
public class OrdenesMecanicoController {

    @FXML private Label lblMecanico;
    @FXML private TableView<Orden> tablaOrdenesAsignadas;
    @FXML private TableColumn<Orden, String> colIdOrden;
    @FXML private TableColumn<Orden, String> colVehiculo, colEstado, colDiagnostico;
    @FXML private ComboBox<String> cbNuevoEstado;
    @FXML private TextArea txtDiagnostico;
    @FXML private Label lblMensaje;

    private final OrdenService ordenService;
    private final SceneManager sceneManager;
    private final SesionUsuario sesion;

    public OrdenesMecanicoController(OrdenService ordenService, SceneManager sceneManager, SesionUsuario sesion) {
        this.ordenService = ordenService;
        this.sceneManager = sceneManager;
        this.sesion = sesion;
    }

    @FXML
    public void initialize() {
        colIdOrden.setCellValueFactory(new PropertyValueFactory<>("idOrden"));
        colVehiculo.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colDiagnostico.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));

        cbNuevoEstado.setItems(FXCollections.observableArrayList(
            "En_reparacion", "Espera_repuestos", "Entregado"
        ));

        if (lblMecanico != null) {
            lblMecanico.setText("Mecánico: " + sesion.getNombre());
        }

        cargarOrdenesAsignadas();
    }

    private void cargarOrdenesAsignadas() {
        String idMecanicoSesion = sesion.getIdReferencia();
        if (idMecanicoSesion == null) {
            lblMensaje.setText("No hay una sesión de mecánico activa.");
            return;
        }
        try {
            tablaOrdenesAsignadas.setItems(FXCollections.observableArrayList(
                ordenService.listarPorMecanico(idMecanicoSesion)
            ));
        } catch (Exception e) {
            lblMensaje.setText("No se pudieron cargar las órdenes: " + e.getMessage());
        }
    }

    @FXML
    public void onActualizarEstado() {
        Orden seleccionada = tablaOrdenesAsignadas.getSelectionModel().getSelectedItem();
        String nuevoEstado = cbNuevoEstado.getValue();

        if (seleccionada == null) {
            lblMensaje.setText("Seleccione una orden de la lista.");
            return;
        }

        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            lblMensaje.setText("Seleccione el nuevo estado.");
            return;
        }

        seleccionada.setEstado(nuevoEstado);
        if (txtDiagnostico.getText() != null && !txtDiagnostico.getText().isBlank()) {
            seleccionada.setDiagnostico(txtDiagnostico.getText());
        }

        if (ordenService.actualizarOrden(seleccionada)) {
            lblMensaje.setText("Orden actualizada con éxito.");
            cargarOrdenesAsignadas();
            txtDiagnostico.clear();
        } else {
            lblMensaje.setText("Error al actualizar la orden en la base de datos.");
        }
    }

    @FXML
    public void onEliminarOrden() {
        Orden seleccionada = tablaOrdenesAsignadas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            lblMensaje.setText("Seleccione una orden de la lista.");
            return;
        }
        if (ordenService.eliminarOrden(seleccionada.getIdOrden())) {
            lblMensaje.setText("Orden eliminada.");
            cargarOrdenesAsignadas();
        } else {
            lblMensaje.setText("No se pudo eliminar la orden.");
        }
    }

    @FXML
    public void onVolverMenu() throws Exception {
        // El mecánico no tiene un menú intermedio: "Volver" lo regresa a la
        // pantalla de selección de perfil, igual que cerrar sesión.
        sceneManager.showLoginView();
    }

    @FXML
    public void onCerrarSesion() throws Exception {
        sceneManager.showLoginView();
    }
}
