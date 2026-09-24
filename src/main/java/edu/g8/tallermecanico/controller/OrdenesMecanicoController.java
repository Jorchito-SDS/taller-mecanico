package main.java.edu.g8.tallermecanico.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.g8.tallermecanico.model.Orden;
import main.java.edu.g8.tallermecanico.service.OrdenService;
import main.java.edu.g8.tallermecanico.util.SceneManager;
import main.java.edu.g8.tallermecanico.util.UserSession;

public class OrdenesMecanicoController {

    @FXML private TableView<Orden> tablaOrdenesAsignadas;
    @FXML private TableColumn<Orden, String> colIdOrden;
    @FXML private TableColumn<Orden, String> colVehiculo, colEstado, colDiagnostico;
    @FXML private ComboBox<String> cbNuevoEstado;
    @FXML private TextArea txtDiagnostico;
    @FXML private Label lblMensaje;

    private final OrdenService ordenService = new OrdenService();

    @FXML
    public void initialize() {
        colIdOrden.setCellValueFactory(new PropertyValueFactory<>("idOrden"));
        colVehiculo.setCellValueFactory(new PropertyValueFactory<>("placa"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colDiagnostico.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));

        cbNuevoEstado.setItems(FXCollections.observableArrayList(
            "EN_PROCESO", "ESPERA_REPUESTOS", "FINALIZADO"
        ));

        cargarOrdenesAsignadas();
    }

    private void cargarOrdenesAsignadas() {
        UserSession session = UserSession.getInstance();
        if (session != null && session.getIdReferencia() != null) {
            String idMecanicoSesion = String.valueOf(session.getIdReferencia());
            tablaOrdenesAsignadas.setItems(FXCollections.observableArrayList(
                ordenService.listarOrdenes().stream()
                    .filter(o -> o.getIdMecanico() != null && o.getIdMecanico().equals(idMecanicoSesion))
                    .toList()
            ));
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
    public void onVolverMenu() {
        SceneManager.cambiarVista("/resources/view/MenuView.fxml", "Menú Principal");
    }
}