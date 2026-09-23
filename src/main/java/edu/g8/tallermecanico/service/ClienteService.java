/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.g8.tallermecanico.service;

import java.util.ArrayList;
import java.util.List;
import main.java.edu.g8.tallermecanico.model.Cliente;
import main.java.edu.g8.tallermecanico.repository.ClienteRepository;


public class ClienteService {
    
    private final ClienteRepository clienteRepository = new ClienteRepository();

    public List<Cliente> buscarClientes(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return clienteRepository.buscarPorNombreOPlaca(filtro.trim());
    }
}