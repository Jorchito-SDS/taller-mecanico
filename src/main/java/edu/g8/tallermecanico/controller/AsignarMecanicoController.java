package main.java.edu.g8.tallermecanico.controller;

import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import main.java.edu.g8.tallermecanico.model.Mecanico;
import main.java.edu.g8.tallermecanico.model.Orden;
import main.java.edu.g8.tallermecanico.repository.MecanicoRepository;
import main.java.edu.g8.tallermecanico.repository.OrdenRepository;
import main.java.edu.g8.tallermecanico.util.SceneManager;
import main.java.edu.g8.tallermecanico.util.SesionUsuario;

public class AsignarMecanicoController {

    @FXML private ComboBox<Orden> cmbOrdenes;
    @FXML private ComboBox<Mecanico> cmbMecanicos;

    private final OrdenRepository ordenRepository;
    private final MecanicoRepository mecanicoRepository;
    private final SceneManager sceneManager;
    private final SesionUsuario sesion;

    public AsignarMecanicoController(OrdenRepository ordenRepository, MecanicoRepository mecanicoRepository,
            SceneManager sceneManager, SesionUsuario sesion) {
        this.ordenRepository = ordenRepository;
        this.mecanicoRepository = mecanicoRepository;
        this.sceneManager = sceneManager;
        this.sesion = sesion;
    }

    @FXML
    public void initialize() {
        cargarCombos();
    }

    private void cargarCombos() {
        try {
            cmbOrdenes.setItems(FXCollections.observableArrayList(ordenRepository.listarOrdenesActivas()));
            cmbMecanicos.setItems(FXCollections.observableArrayList(mecanicoRepository.listarMecanicos()));
        } catch (SQLException e) {
            sceneManager.showInfoAlert("Error de Base de Datos", null, "Error al cargar listas para asignación.", AlertType.ERROR);
        }
    }

    @FXML
    public void onAsignar(ActionEvent event) {
        Orden ordenSeleccionada = cmbOrdenes.getValue();
        Mecanico mecanicoSeleccionado = cmbMecanicos.getValue();

        if (ordenSeleccionada == null || mecanicoSeleccionado == null) {
            sceneManager.showInfoAlert("Selección Requerida", null, "Debes seleccionar una orden y un mecánico.", AlertType.WARNING);
            return;
        }

        try {
            if (ordenRepository.asignarMecanico(ordenSeleccionada.getIdOrden(), mecanicoSeleccionado.getIdMecanico())) {
                sceneManager.showInfoAlert("Asignación Exitosa", null, "Se asignó el mecánico a la orden.", AlertType.INFORMATION);
                cargarCombos();
            }
        } catch (SQLException e) {
            sceneManager.showInfoAlert("Error de Asignación", null, "No se pudo actualizar la orden en la base de datos.", AlertType.ERROR);
        }
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
