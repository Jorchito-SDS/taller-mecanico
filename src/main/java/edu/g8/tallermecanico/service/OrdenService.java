package main.java.edu.g8.tallermecanico.service;

import java.sql.SQLException;
import java.util.List;
import main.java.edu.g8.tallermecanico.model.Orden;
import main.java.edu.g8.tallermecanico.repository.OrdenRepository;

public class OrdenService {

    private final OrdenRepository ordenRepository = new OrdenRepository();

    public List<Orden> listarActivas() throws SQLException {
        return ordenRepository.listarActivas();
    }

    public List<Orden> listarPorMecanico(String idMecanico) throws SQLException {
        return ordenRepository.listarPorMecanico(idMecanico);
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
}