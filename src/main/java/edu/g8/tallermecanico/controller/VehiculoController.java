/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.g8.tallermecanico.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.java.edu.g8.tallermecanico.model.Vehiculo;
import main.java.edu.g8.tallermecanico.service.VehiculoService;

/**
 *
 * @author informatica
 */
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController() {
        this.vehiculoService = new VehiculoService();
    }

    // Campos de texto vinculados con la vista FXML (fx:id)
    @FXML
    private TextField txtIdVehiculo;
    @FXML
    private TextField txtIdCliente;
    @FXML
    private TextField txtPlaca;
    @FXML
    private TextField txtMarca;
    @FXML
    private TextField txtModelo;
    @FXML
    private TextField txtAnio;
    @FXML
    private TextField txtKilometraje;

    // Método que se ejecuta al presionar el botón de Guardar en la vista
 @FXML
public void guardarVehiculo() {
    try {
        // Ya no leemos txtIdVehiculo porque es AUTO_INCREMENT en la BD
        int idCliente = Integer.parseInt(txtIdCliente.getText());
        String placa = txtPlaca.getText();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        int anio = Integer.parseInt(txtAnio.getText());
        int kilometraje = Integer.parseInt(txtKilometraje.getText());

        // Pasamos 0 como id_vehiculo (al ser auto_increment, la BD le asignará el valor real)
        // Nota: Asegúrate de que el constructor de Vehiculo acepte un int o String para el id según lo tengas definido.
        Vehiculo vehiculo = new Vehiculo(0, idCliente, placa, marca, modelo, anio, kilometraje);

        boolean exito = vehiculoService.registrarVehiculo(vehiculo);

        if (exito) {
            System.out.println("¡Vehículo guardado correctamente!");
        } else {
            System.out.println("Error al intentar guardar el vehículo.");
        }

    } catch (NumberFormatException e) {
        System.out.println("Error de formato: Verifica que los campos numéricos sean válidos.");
    }
}
}