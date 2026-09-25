package main.java.edu.g8.tallermecanico.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.g8.tallermecanico.model.Mecanico;
import main.java.edu.g8.tallermecanico.repository.MecanicoRepository;

public class MecanicoService {

    private final MecanicoRepository mecanicoRepository = new MecanicoRepository();

    public boolean registrarMecanico(Mecanico mecanico, String password) {
        try {
            return mecanicoRepository.registrarMecanico(mecanico, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Mecanico autenticar(String nombre, String password) {
        try {
            return mecanicoRepository.autenticar(nombre, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Mecanico> listarMecanicos() {
        try {
            return mecanicoRepository.listarMecanicos();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean guardar(Mecanico mecanico) {
        try {
            return mecanicoRepository.guardar(mecanico);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarMecanico(Mecanico mecanico, String nuevaPassword) throws SQLException {
        return mecanicoRepository.actualizar(mecanico, nuevaPassword);
    }

    public boolean eliminarMecanico(String idMecanico) throws SQLException {
        return mecanicoRepository.eliminar(idMecanico);
    }
}