package main.java.edu.g8.tallermecanico.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.g8.tallermecanico.model.Vehiculo;
import main.java.edu.g8.tallermecanico.repository.VehiculoRepository;

public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService() {
        this.vehiculoRepository = new VehiculoRepository();
    }

    public boolean registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
            System.out.println("Error: La placa del vehículo no puede estar vacía.");
            return false;
        }

        try {
            Vehiculo existente = vehiculoRepository.buscarPorPlaca(vehiculo.getPlaca());
            if (existente != null) {
                System.out.println("Error: Ya existe un vehículo registrado con esta placa.");
                return false;
            }
            return vehiculoRepository.guardar(vehiculo);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Vehiculo> listarVehiculos() {
        try {
            return vehiculoRepository.listar();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Vehiculo buscarVehiculoPorPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return null;
        }
        try {
            return vehiculoRepository.buscarPorPlaca(placa);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean actualizarVehiculo(Vehiculo vehiculo) {
        if (vehiculo.getIdVehiculo() == null || vehiculo.getIdVehiculo().trim().isEmpty()) {
            System.out.println("Error: El ID del vehículo es obligatorio para actualizar.");
            return false;
        }
        try {
            return vehiculoRepository.actualizar(vehiculo);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarVehiculo(String idVehiculo) {
        if (idVehiculo == null || idVehiculo.trim().isEmpty()) {
            return false;
        }
        try {
            return vehiculoRepository.eliminar(idVehiculo);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}