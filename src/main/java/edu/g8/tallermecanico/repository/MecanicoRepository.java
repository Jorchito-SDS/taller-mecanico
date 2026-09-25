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

    public boolean registrarMecanico(Mecanico mecanico, String password) throws SQLException {
        String sql = "INSERT INTO Mecanico (nombre, especialidad, telefono, disponible, contrasena_hash) VALUES (?, ?, ?, ?, SHA2(?, 256))";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mecanico.getNombre());
            ps.setString(2, mecanico.getEspecialidad());
            ps.setString(3, mecanico.getTelefono());
            ps.setInt(4, mecanico.getDisponible());
            ps.setString(5, password);

            return ps.executeUpdate() > 0;
        }
    }

    public Mecanico autenticar(String nombre, String password) throws SQLException {
        String sql = "SELECT id_mecanico, nombre, especialidad, telefono, disponible FROM Mecanico WHERE nombre = ? AND contrasena_hash = SHA2(?, 256)";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Mecanico(
                        rs.getString("id_mecanico"),
                        rs.getString("nombre"),
                        rs.getString("especialidad"),
                        rs.getString("telefono"),
                        rs.getInt("disponible")
                    );
                }
            }
        }
        return null;
    }

    public boolean guardar(Mecanico mecanico) throws SQLException {
        String sql = "INSERT INTO Mecanico (nombre, especialidad, telefono, disponible, contrasena_hash) VALUES (?, ?, ?, ?, SHA2(?, 256))";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, mecanico.getNombre());
            ps.setString(2, mecanico.getEspecialidad());
            ps.setString(3, mecanico.getTelefono());
            ps.setInt(4, mecanico.getDisponible());
            ps.setString(5, mecanico.getContrasenaHash() != null ? mecanico.getContrasenaHash() : "");

            return ps.executeUpdate() > 0;
        }
    }

    public boolean guardarMecanico(Mecanico mecanico) throws SQLException {
        return guardar(mecanico);
    }

    public boolean actualizar(Mecanico mecanico, String nuevaPassword) throws SQLException {
        String sql = (nuevaPassword != null && !nuevaPassword.isEmpty())
                ? "UPDATE Mecanico SET nombre = ?, especialidad = ?, telefono = ?, disponible = ?, contrasena_hash = SHA2(?, 256) WHERE id_mecanico = ?"
                : "UPDATE Mecanico SET nombre = ?, especialidad = ?, telefono = ?, disponible = ? WHERE id_mecanico = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int i = 1;
            ps.setString(i++, mecanico.getNombre());
            ps.setString(i++, mecanico.getEspecialidad());
            ps.setString(i++, mecanico.getTelefono());
            ps.setInt(i++, mecanico.getDisponible());
            if (nuevaPassword != null && !nuevaPassword.isEmpty()) {
                ps.setString(i++, nuevaPassword);
            }
            ps.setString(i, mecanico.getIdMecanico());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String idMecanico) throws SQLException {
        try (Connection conn = ConnectionDb.getConnection()) {
            // Antes de borrar al mecánico, se desasignan sus órdenes para no
            // dejar referencias rotas (en vez de bloquear el borrado).
            try (PreparedStatement psDesasignar = conn.prepareStatement(
                    "UPDATE Orden SET id_mecanico = NULL WHERE id_mecanico = ?")) {
                psDesasignar.setString(1, idMecanico);
                psDesasignar.executeUpdate();
            }
            try (PreparedStatement psEliminar = conn.prepareStatement(
                    "DELETE FROM Mecanico WHERE id_mecanico = ?")) {
                psEliminar.setString(1, idMecanico);
                return psEliminar.executeUpdate() > 0;
            }
        }
    }
}