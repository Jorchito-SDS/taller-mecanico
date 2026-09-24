/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.g8.tallermecanico.service;

import java.util.List;
import main.java.edu.g8.tallermecanico.model.Vehiculo;
import main.java.edu.g8.tallermecanico.repository.VehiculoRepository;

/**
 *
 * @author informatica
 */
public class VehiculoService {
    private final VehiculoRepository vehiculoRepository;
    
    public VehiculoService(){
        this.vehiculoRepository = new VehiculoRepository();
    }
    
    public boolean registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
            System.out.println("Error: La placa del vehículo no puede estar vacía.");
            return false;
        }

        Vehiculo existente = vehiculoRepository.buscarPorPlaca(vehiculo.getPlaca());
        if (existente != null) {
            System.out.println("Error: Ya existe un vehículo registrado con esta placa.");
            return false;
        }

        return vehiculoRepository.guardar(vehiculo);
    }

    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepository.listar();
    }

    public Vehiculo buscarVehiculoPorPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            return null;
        }
        return vehiculoRepository.buscarPorPlaca(placa);
    }

    public boolean actualizarVehiculo(Vehiculo vehiculo) {
        if (vehiculo.getId_vehiculo() == null || vehiculo.getId_vehiculo().trim().isEmpty()) {
            System.out.println("Error: El ID del vehículo es obligatorio para actualizar.");
            return false;
        }
        return vehiculoRepository.actualizar(vehiculo);
    }

    public boolean eliminarVehiculo(String idVehiculo) {
        if (idVehiculo == null || idVehiculo.trim().isEmpty()) {
            return false;
        }
        return vehiculoRepository.eliminar(idVehiculo);
    }
}