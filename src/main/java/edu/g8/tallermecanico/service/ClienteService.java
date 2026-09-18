/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.g8.tallermecanico.service;

import main.java.edu.g8.tallermecanico.model.Cliente;
import main.java.edu.g8.tallermecanico.repository.ClienteRepository;

/**
 *
 * @author informatica
 */
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(){
        this.clienteRepository = new ClienteRepository();
    }

    public boolean registrarCliente(int id_cliente, String nombre, String telefono, String email, String direccion) throws IllegalArgumentException {
        // Reglas de negocio / Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono del cliente es obligatorio.");
        }

        Cliente nuevoCliente = new Cliente(id_cliente, nombre, telefono, email, direccion);
        return clienteRepository.insertar(nuevoCliente);
    }
}
