package main.java.edu.g8.tallermecanico.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.g8.tallermecanico.model.Mecanico;
import main.java.edu.g8.tallermecanico.repository.MecanicoRepository;

public class MecanicoService {

    private final MecanicoRepository mecanicoRepository = new MecanicoRepository();

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
}