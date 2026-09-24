package main.java.edu.g8.tallermecanico.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {

    @FXML private Button btnVehiculos;
    @FXML private Button btnOrdenes;
    @FXML private Button btnAsignarMecanico;
    @FXML private Button btnOrdenesPorMecanico;
    @FXML private Button btnRepuestos;

    public void configurarMenuPorRol(String rol) {
        switch (rol) {
            case "CLIENTE":
                // El cliente solo ve/registra sus vehículos y consulta estatus de órdenes
                if (btnVehiculos != null) btnVehiculos.setVisible(true);
                if (btnOrdenes != null) btnOrdenes.setVisible(true);
                if (btnAsignarMecanico != null) btnAsignarMecanico.setVisible(false);
                if (btnOrdenesPorMecanico != null) btnOrdenesPorMecanico.setVisible(false);
                if (btnRepuestos != null) btnRepuestos.setVisible(false);
                break;

            case "MECANICO":
                // El mecánico consulta sus órdenes asignadas y repuestos
                if (btnVehiculos != null) btnVehiculos.setVisible(false);
                if (btnOrdenes != null) btnOrdenes.setVisible(true);
                if (btnAsignarMecanico != null) btnAsignarMecanico.setVisible(false);
                if (btnOrdenesPorMecanico != null) btnOrdenesPorMecanico.setVisible(true);
                if (btnRepuestos != null) btnRepuestos.setVisible(true);
                break;

            case "GERENTE":
            default:
                // El Gerente / Administrador tiene acceso total
                if (btnVehiculos != null) btnVehiculos.setVisible(true);
                if (btnOrdenes != null) btnOrdenes.setVisible(true);
                if (btnAsignarMecanico != null) btnAsignarMecanico.setVisible(true);
                if (btnOrdenesPorMecanico != null) btnOrdenesPorMecanico.setVisible(true);
                if (btnRepuestos != null) btnRepuestos.setVisible(true);
                break;
        }
    }
}