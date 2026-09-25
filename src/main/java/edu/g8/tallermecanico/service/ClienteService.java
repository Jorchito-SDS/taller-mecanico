package main.java.edu.g8.tallermecanico.service;

import java.util.ArrayList;
import java.util.List;
import main.java.edu.g8.tallermecanico.model.Cliente;
import main.java.edu.g8.tallermecanico.repository.ClienteRepository;

public class ClienteService {

    private final ClienteRepository clienteRepository = new ClienteRepository();

    public List<Cliente> listarClientes() {
        try {
            return clienteRepository.listar();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Cliente> buscarClientes(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return clienteRepository.buscarPorNombreOPlaca(filtro.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean registrarCliente(Cliente cliente, String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        try {
            return clienteRepository.guardar(cliente, password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registrarCliente(Cliente cliente) {
        return registrarCliente(cliente, "123456");
    }

    public boolean actualizarCliente(Cliente cliente) {
        try {
            return clienteRepository.actualizar(cliente);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Acepta int directamente (corrige el error en GestionClientesController)
    public boolean eliminarCliente(int idCliente) {
        return eliminarCliente(String.valueOf(idCliente));
    }

    // Acepta String
    public boolean eliminarCliente(String idCliente) {
        try {
            return clienteRepository.eliminar(idCliente);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Cliente autenticar(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        try {
            return clienteRepository.autenticar(email, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Cliente autenticarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        try {
            return clienteRepository.buscarPorEmail(email.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}