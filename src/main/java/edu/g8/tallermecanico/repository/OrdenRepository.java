package main.java.edu.g8.tallermecanico.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.g8.tallermecanico.config.ConnectionDb;
import main.java.edu.g8.tallermecanico.model.Orden;

public class OrdenRepository {

    public List<Orden> listarActivas() throws SQLException {
        List<Orden> lista = new ArrayList<>();
        String sql = "SELECT o.id_orden, c.nombre AS cliente, v.placa, m.nombre AS mecanico, o.estado, o.fecha_recepcion " +
                     "FROM Orden o " +
                     "JOIN Vehiculo v ON o.id_vehiculo = v.id_vehiculo " +
                     "JOIN Cliente c ON v.id_cliente = c.id_cliente " +
                     "LEFT JOIN Mecanico m ON o.id_mecanico = m.id_mecanico " +
                     "WHERE o.estado != 'Entregado'";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Orden orden = new Orden(
                    rs.getString("id_orden"),
                    rs.getString("placa"),
                    rs.getString("cliente"),
                    rs.getString("mecanico") != null ? rs.getString("mecanico") : "Sin Asignar",
                    rs.getString("estado"),
                    rs.getString("fecha_recepcion")
                );
                lista.add(orden);
            }
        }
        return lista;
    }

    public List<Orden> listarOrdenesActivas() throws SQLException {
        return listarActivas();
    }

    public List<Orden> listarPorCliente(String idCliente) throws SQLException {
        List<Orden> lista = new ArrayList<>();
        String sql = "SELECT o.id_orden, c.nombre AS cliente, v.placa, m.nombre AS mecanico, o.estado, o.fecha_recepcion, o.diagnostico " +
                     "FROM Orden o " +
                     "JOIN Vehiculo v ON o.id_vehiculo = v.id_vehiculo " +
                     "JOIN Cliente c ON v.id_cliente = c.id_cliente " +
                     "LEFT JOIN Mecanico m ON o.id_mecanico = m.id_mecanico " +
                     "WHERE c.id_cliente = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Orden orden = new Orden(
                        rs.getString("id_orden"),
                        rs.getString("placa"),
                        rs.getString("cliente"),
                        rs.getString("mecanico") != null ? rs.getString("mecanico") : "Sin Asignar",
                        rs.getString("estado"),
                        rs.getString("fecha_recepcion")
                    );
                    orden.setDiagnostico(rs.getString("diagnostico"));
                    lista.add(orden);
                }
            }
        }
        return lista;
    }

    public List<Orden> listarPorMecanico(String idMecanico) throws SQLException {
        List<Orden> lista = new ArrayList<>();
        String sql = "SELECT o.id_orden, c.nombre AS cliente, v.placa, m.nombre AS mecanico, o.estado, o.fecha_recepcion, o.diagnostico " +
                     "FROM Orden o " +
                     "JOIN Vehiculo v ON o.id_vehiculo = v.id_vehiculo " +
                     "JOIN Cliente c ON v.id_cliente = c.id_cliente " +
                     "LEFT JOIN Mecanico m ON o.id_mecanico = m.id_mecanico " +
                     "WHERE o.id_mecanico = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idMecanico);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Orden orden = new Orden(
                        rs.getString("id_orden"),
                        rs.getString("placa"),
                        rs.getString("cliente"),
                        rs.getString("mecanico") != null ? rs.getString("mecanico") : "Sin Asignar",
                        rs.getString("estado"),
                        rs.getString("fecha_recepcion")
                    );
                    orden.setDiagnostico(rs.getString("diagnostico"));
                    orden.setIdMecanico(idMecanico);
                    lista.add(orden);
                }
            }
        }
        return lista;
    }

    public boolean crearOrdenPorPlaca(String placa, String motivo, LocalDate fechaCita) throws SQLException {
        String sqlVehiculo = "SELECT id_vehiculo FROM Vehiculo WHERE placa = ?";
        String sqlInsert = "INSERT INTO Orden (id_vehiculo, fecha_recepcion, estado, diagnostico) VALUES (?, ?, 'Pendiente', ?)";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement psVehiculo = conn.prepareStatement(sqlVehiculo)) {

            psVehiculo.setString(1, placa);
            try (ResultSet rs = psVehiculo.executeQuery()) {
                if (rs.next()) {
                    String idVehiculo = rs.getString("id_vehiculo");
                    try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                        psInsert.setString(1, idVehiculo);
                        psInsert.setString(2, fechaCita != null ? fechaCita.toString() : LocalDate.now().toString());
                        psInsert.setString(3, motivo);
                        return psInsert.executeUpdate() > 0;
                    }
                }
            }
        }
        return false;
    }

    public boolean crearOrdenPorPlaca(String placa, String diagnostico) throws SQLException {
        return crearOrdenPorPlaca(placa, diagnostico, LocalDate.now());
    }

    public boolean guardar(Orden orden) throws SQLException {
        String sql = "INSERT INTO Orden (id_vehiculo, id_mecanico, fecha_recepcion, estado, diagnostico) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, orden.getIdVehiculo());
            ps.setString(2, orden.getIdMecanico());
            ps.setString(3, orden.getFechaRecepcion());
            ps.setString(4, orden.getEstado() != null ? orden.getEstado() : "Recibido");
            ps.setString(5, orden.getDiagnostico());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean actualizarEstado(String idOrden, String nuevoEstado) throws SQLException {
        String sql = "UPDATE Orden SET estado = ? WHERE id_orden = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setString(2, idOrden);

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String idOrden) throws SQLException {
        String sql = "DELETE FROM Orden WHERE id_orden = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idOrden);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean asignarMecanico(String idOrden, String idMecanico) throws SQLException {
        String sql = "UPDATE Orden SET id_mecanico = ?, estado = 'En_reparacion' WHERE id_orden = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idMecanico);
            ps.setString(2, idOrden);

            return ps.executeUpdate() > 0;
        }
    }
}