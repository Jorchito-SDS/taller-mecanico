package main.java.edu.g8.tallermecanico.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import main.java.edu.g8.tallermecanico.model.Orden;
import main.java.edu.g8.tallermecanico.repository.OrdenRepository;

public class OrdenService {

    private final OrdenRepository ordenRepository = new OrdenRepository();

    public List<Orden> listarPorCliente(String idCliente) {
        try {
            return ordenRepository.listarPorCliente(idCliente);
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean crearOrdenPorPlaca(String placa, String motivo, LocalDate fechaCita) {
        try {
            return ordenRepository.crearOrdenPorPlaca(placa, motivo, fechaCita);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean crearOrdenPorPlaca(String placa, String diagnostico) {
        try {
            return ordenRepository.crearOrdenPorPlaca(placa, diagnostico);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Orden> listarActivas() throws SQLException {
        return ordenRepository.listarActivas();
    }

    public List<Orden> listarPorMecanico(String idMecanico) throws SQLException {
        return ordenRepository.listarPorMecanico(idMecanico);
    }

    public List<Orden> listarOrdenes() {
        try {
            return ordenRepository.listarActivas();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean guardar(Orden orden) throws SQLException {
        return ordenRepository.guardar(orden);
    }

    public boolean actualizarEstado(String idOrden, String nuevoEstado) throws SQLException {
        return ordenRepository.actualizarEstado(idOrden, nuevoEstado);
    }

    public boolean asignarMecanico(String idOrden, String idMecanico) throws SQLException {
        return ordenRepository.asignarMecanico(idOrden, idMecanico);
    }

    public boolean eliminarOrden(String idOrden) {
        try {
            return ordenRepository.eliminar(idOrden);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarOrden(Orden orden) {
        try {
            return ordenRepository.actualizarEstado(String.valueOf(orden.getIdOrden()), orden.getEstado());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}