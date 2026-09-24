package main.java.edu.g8.tallermecanico.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.g8.tallermecanico.config.ConnectionDb;
import main.java.edu.g8.tallermecanico.model.Mecanico;

public class MecanicoRepository {

    public List<Mecanico> listarMecanicos() throws SQLException {
        List<Mecanico> lista = new ArrayList<>();
        String sql = "SELECT id_mecanico, nombre, especialidad, telefono, disponible FROM Mecanico";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Mecanico m = new Mecanico(
                    rs.getString("id_mecanico"),
                    rs.getString("nombre"),
                    rs.getString("especialidad"),
                    rs.getString("telefono"),
                    rs.getInt("disponible")
                );
                lista.add(m);
            }
        }
        return lista;
    }

    public boolean guardar(Mecanico mecanico) throws SQLException {
        String sql = "INSERT INTO Mecanico (nombre, especialidad, telefono, disponible, contrasena_hash) VALUES (?, ?, ?, ?, SHA2(?, 256))";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mecanico.getNombre());
            ps.setString(2, mecanico.getEspecialidad());
            ps.setString(3, mecanico.getTelefono());
            ps.setInt(4, mecanico.getDisponible());
            ps.setString(5, mecanico.getContrasenaHash());

            return ps.executeUpdate() > 0;
        }
    }

    // Alias por compatibilidad
    public boolean guardarMecanico(Mecanico mecanico) throws SQLException {
        return guardar(mecanico);
    }
}