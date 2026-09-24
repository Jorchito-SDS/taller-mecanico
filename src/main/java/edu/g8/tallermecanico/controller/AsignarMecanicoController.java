package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import main.java.edu.g8.tallermecanico.model.Mecanico;
import main.java.edu.g8.tallermecanico.model.Orden;
import main.java.edu.g8.tallermecanico.service.MecanicoService;
import main.java.edu.g8.tallermecanico.service.OrdenService;

public class AsignarMecanicoController {

    @FXML private ComboBox<Orden> cmbOrden;
    @FXML private ComboBox<Mecanico> cmbMecanico;
    @FXML private Label lblMensaje;

    private final OrdenService ordenService = new OrdenService();
    private final MecanicoService mecanicoService = new MecanicoService();

    @FXML
    public void initialize() {
        // Cómo se muestra cada Orden dentro del ComboBox (placa + cliente + estado)
        cmbOrden.setConverter(new StringConverter<Orden>() {
            @Override
            public String toString(Orden o) {
                if (o == null) return "";
                return o.getPlacaVehiculo() + " - " + o.getNombreCliente() + " (" + o.getEstado() + ")";
            }
            @Override
            public Orden fromString(String s) { return null; } // no se usa: el combo no es editable
        });

        // Cómo se muestra cada Mecánico dentro del ComboBox
        cmbMecanico.setConverter(new StringConverter<Mecanico>() {
            @Override
            public String toString(Mecanico m) {
                if (m == null) return "";
                return m.getNombre() + " - " + m.getEspecialidad();
            }
            @Override
            public Mecanico fromString(String s) { return null; }
        });

        cargarCombos();
    }

    private void cargarCombos() {
        ObservableList<Orden> ordenes = FXCollections.observableArrayList(
                ordenService.listarActivas());
        cmbOrden.setItems(ordenes);

        ObservableList<Mecanico> mecanicos = FXCollections.observableArrayList(
                mecanicoService.listarMecanicos());
        cmbMecanico.setItems(mecanicos);
    }

    @FXML
    public void onAsignar(ActionEvent event) {
        Orden ordenSeleccionada = cmbOrden.getValue();
        Mecanico mecanicoSeleccionado = cmbMecanico.getValue();

        if (ordenSeleccionada == null || mecanicoSeleccionado == null) {
            mostrarAlerta("Debe seleccionar una orden y un mecánico.");
            return;
        }

        try {
            boolean exito = ordenService.asignarMecanico(
                    ordenSeleccionada.getIdOrden(),
                    mecanicoSeleccionado.getIdMecanico()
            );

            if (exito) {
                lblMensaje.setText("Mecánico asignado correctamente.");
                cargarCombos(); // refresca para que la orden ya muestre el mecánico nuevo si se reabre
                cmbOrden.setValue(null);
                cmbMecanico.setValue(null);
            } else {
                lblMensaje.setText("No se pudo asignar el mecánico. Intente de nuevo.");
            }

        } catch (IllegalArgumentException e) {
            mostrarAlerta(e.getMessage());
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Asignar Mecánico");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}