package main.java.edu.g8.tallermecanico.repository;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import main.java.edu.g8.tallermecanico.config.ConnectionDb;
import main.java.edu.g8.tallermecanico.model.Orden;

public class OrdenRepository {

    private static final SimpleDateFormat FORMATO_FECHA =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /** Crea una nueva orden de servicio. Requiere que el vehículo ya exista. */
    public boolean guardar(Orden orden) {
        String sql = "INSERT INTO Orden (id_vehiculo, id_mecanico, fecha_recepcion, estado, diagnostico) "
                    + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, orden.getIdVehiculo());
            if (orden.getIdMecanico() != null && !orden.getIdMecanico().isEmpty()) {
                stmt.setString(2, orden.getIdMecanico());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }
            stmt.setString(3, FORMATO_FECHA.format(new Date()));
            stmt.setString(4, orden.getEstado());
            stmt.setString(5, orden.getDiagnostico());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Lista todas las órdenes activas (no entregadas) con datos de cliente, vehículo y mecánico. */
    public List<Orden> listarActivas() {
        String sql = "SELECT o.id_orden, o.id_vehiculo, o.id_mecanico, o.fecha_recepcion, "
                    + "o.fecha_entrega, o.estado, o.diagnostico, "
                    + "v.placa, c.nombre AS nombre_cliente, m.nombre AS nombre_mecanico "
                    + "FROM Orden o "
                    + "JOIN Vehiculo v ON o.id_vehiculo = v.id_vehiculo "
                    + "JOIN Cliente c ON v.id_cliente = c.id_cliente "
                    + "LEFT JOIN Mecanico m ON o.id_mecanico = m.id_mecanico "
                    + "WHERE o.estado != 'Entregado' "
                    + "ORDER BY o.fecha_recepcion DESC";
        return ejecutarConsulta(sql);
    }

    /** Lista las órdenes asignadas a un mecánico específico (TMM-14). */
    public List<Orden> listarPorMecanico(String idMecanico) {
        String sql = "SELECT o.id_orden, o.id_vehiculo, o.id_mecanico, o.fecha_recepcion, "
                    + "o.fecha_entrega, o.estado, o.diagnostico, "
                    + "v.placa, c.nombre AS nombre_cliente, m.nombre AS nombre_mecanico "
                    + "FROM Orden o "
                    + "JOIN Vehiculo v ON o.id_vehiculo = v.id_vehiculo "
                    + "JOIN Cliente c ON v.id_cliente = c.id_cliente "
                    + "LEFT JOIN Mecanico m ON o.id_mecanico = m.id_mecanico "
                    + "WHERE o.id_mecanico = ? "
                    + "ORDER BY o.fecha_recepcion DESC";

        List<Orden> resultado = new ArrayList<>();
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idMecanico);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapearOrden(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    /** Actualiza únicamente el estado de una orden (usado en TMM-15 más adelante). */
    public boolean actualizarEstado(String idOrden, String nuevoEstado) {
        String sql = "UPDATE Orden SET estado = ? WHERE id_orden = ?";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado);
            stmt.setString(2, idOrden);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Asigna o reasigna un mecánico a una orden existente (usado en TMM-12). */
    public boolean asignarMecanico(String idOrden, String idMecanico) {
        String sql = "UPDATE Orden SET id_mecanico = ? WHERE id_orden = ?";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, idMecanico);
            stmt.setString(2, idOrden);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Orden> ejecutarConsulta(String sql) {
        List<Orden> resultado = new ArrayList<>();
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resultado.add(mapearOrden(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    private Orden mapearOrden(ResultSet rs) throws SQLException {
        Orden o = new Orden(
                rs.getString("id_orden"),
                rs.getString("id_vehiculo"),
                rs.getString("id_mecanico"),
                rs.getString("fecha_recepcion"),
                rs.getString("fecha_entrega"),
                rs.getString("estado"),
                rs.getString("diagnostico")
        );
        o.setPlacaVehiculo(rs.getString("placa"));
        o.setNombreCliente(rs.getString("nombre_cliente"));
        o.setNombreMecanico(rs.getString("nombre_mecanico"));
        return o;
    }
}