package main.java.edu.g8.tallermecanico.controller;

import java.sql.Connection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.java.edu.g8.tallermecanico.config.ConnectionDb;
import main.java.edu.g8.tallermecanico.model.Factura;
import main.java.edu.g8.tallermecanico.service.FacturaService;

public class FacturaController {

    @FXML private TextField txtIdOrden;
    @FXML private TextField txtTotal;

    private final FacturaService facturaService = new FacturaService();

    @FXML
    private void handleGenerarFactura() {
        try (Connection conn = ConnectionDb.getInstance().getConnection()) {
            Factura factura = new Factura();
            factura.setIdOrden(Integer.parseInt(txtIdOrden.getText()));
            factura.setTotal(Double.parseDouble(txtTotal.getText()));

            if (facturaService.procesarFactura(factura, conn)) {
                mostrarAlerta("Éxito", "Factura registrada e impresa exitosamente.", Alert.AlertType.INFORMATION);
                txtIdOrden.clear();
                txtTotal.clear();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Ingrese valores numéricos válidos en la Orden y el Total.", Alert.AlertType.WARNING);
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}