package main.java.edu.g8.tallermecanico.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import main.java.edu.g8.tallermecanico.model.Mecanico;
import main.java.edu.g8.tallermecanico.model.Orden;
import main.java.edu.g8.tallermecanico.service.MecanicoService;
import main.java.edu.g8.tallermecanico.service.OrdenService;

public class OrdenesMecanicoController {

    @FXML private ComboBox<Mecanico> cmbMecanico;
    @FXML private Label lblMensaje;

    @FXML private TableView<Orden> tablaOrdenes;
    @FXML private TableColumn<Orden, String> colPlaca;
    @FXML private TableColumn<Orden, String> colCliente;
    @FXML private TableColumn<Orden, String> colEstado;
    @FXML private TableColumn<Orden, String> colFechaRecepcion;

    private final OrdenService ordenService = new OrdenService();
    private final MecanicoService mecanicoService = new MecanicoService();

    @FXML
    public void initialize() {
        cmbMecanico.setConverter(new StringConverter<Mecanico>() {
            @Override
            public String toString(Mecanico m) {
                if (m == null) return "";
                return m.getNombre() + " - " + m.getEspecialidad();
            }
            @Override
            public Mecanico fromString(String s) { return null; } // no se usa: el combo no es editable
        });

        colPlaca.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPlacaVehiculo()));
        colCliente.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getNombreCliente()));
        colEstado.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getEstado()));
        colFechaRecepcion.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getFechaRecepcion()));

        ObservableList<Mecanico> mecanicos = FXCollections.observableArrayList(
                mecanicoService.listarMecanicos());
        cmbMecanico.setItems(mecanicos);
    }

    @FXML
    public void onVerOrdenes() {
        Mecanico seleccionado = cmbMecanico.getValue();
        if (seleccionado == null) {
            lblMensaje.setText("Seleccione un mecánico primero.");
            return;
        }

        try {
            ObservableList<Orden> lista = FXCollections.observableArrayList(
                    ordenService.listarPorMecanico(seleccionado.getIdMecanico()));
            tablaOrdenes.setItems(lista);

            lblMensaje.setText(lista.isEmpty()
                    ? "Este mecánico no tiene órdenes asignadas."
                    : lista.size() + " orden(es) encontrada(s).");
        } catch (IllegalArgumentException e) {
            lblMensaje.setText(e.getMessage());
        }
    }
}