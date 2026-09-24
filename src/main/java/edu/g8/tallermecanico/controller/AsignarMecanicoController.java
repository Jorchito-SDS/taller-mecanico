package main.java.edu.g8.tallermecanico.controller;

import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import main.java.edu.g8.tallermecanico.model.Mecanico;
import main.java.edu.g8.tallermecanico.model.Orden;
import main.java.edu.g8.tallermecanico.repository.MecanicoRepository;
import main.java.edu.g8.tallermecanico.repository.OrdenRepository;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class AsignarMecanicoController {

    @FXML private ComboBox<Orden> cmbOrdenes;
    @FXML private ComboBox<Mecanico> cmbMecanicos;

    private final OrdenRepository ordenRepository = new OrdenRepository();
    private final MecanicoRepository mecanicoRepository = new MecanicoRepository();

    @FXML
    public void initialize() {
        cargarCombos();
    }

    private void cargarCombos() {
        try {
            cmbOrdenes.setItems(FXCollections.observableArrayList(ordenRepository.listarOrdenesActivas()));
            cmbMecanicos.setItems(FXCollections.observableArrayList(mecanicoRepository.listarMecanicos()));
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Base de Datos", "Error al cargar listas para asignación.");
        }
    }

    @FXML
    public void onAsignar(ActionEvent event) {
        Orden ordenSeleccionada = cmbOrdenes.getValue();
        Mecanico mecanicoSeleccionado = cmbMecanicos.getValue();

        if (ordenSeleccionada == null || mecanicoSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Requerida", "Debes seleccionar una orden y un mecánico.");
            return;
        }

        try {
            if (ordenRepository.asignarMecanico(ordenSeleccionada.getIdOrden(), mecanicoSeleccionado.getIdMecanico())) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Asignación Exitosa", "Se asignó el mecánico a la orden.");
                cargarCombos();
            }
        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Asignación", "No se pudo actualizar la orden en la base de datos.");
        }
    }

    @FXML
    public void onVolverMenu(ActionEvent event) {
        SceneManager.cambiarVista("/view/MenuView.fxml", "Gestión General - Taller Mecánico");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}