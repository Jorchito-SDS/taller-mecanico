package main.java.edu.g8.tallermecanico.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import main.java.edu.g8.tallermecanico.config.ConnectionDb;
import main.java.edu.g8.tallermecanico.model.Vehiculo;

public class VehiculoRepository {

    private static final String COLUMNAS =
            "id_vehiculo, id_cliente, marca, modelo, anio, placa, kilometraje, estado";

    private Vehiculo mapear(ResultSet rs) throws SQLException {
        return new Vehiculo(
                rs.getString("id_vehiculo"),
                rs.getString("id_cliente"),
                rs.getString("marca"),
                rs.getString("modelo"),
                rs.getInt("anio"),
                rs.getString("placa"),
                rs.getInt("kilometraje"),
                rs.getString("estado")
        );
    }

    public List<Vehiculo> listar() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT " + COLUMNAS + " FROM Vehiculo ORDER BY placa";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Vehiculo> buscarPorPlacaLike(String filtro) throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT " + COLUMNAS + " FROM Vehiculo WHERE placa LIKE ? ORDER BY placa";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + filtro + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<Vehiculo> listarPorCliente(String idCliente) throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT " + COLUMNAS + " FROM Vehiculo WHERE id_cliente = ? ORDER BY placa";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public boolean guardar(Vehiculo vehiculo) throws SQLException {
        String sql = "INSERT INTO Vehiculo (id_vehiculo, id_cliente, marca, modelo, anio, placa, kilometraje, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String idVehiculo = (vehiculo.getIdVehiculo() != null && !vehiculo.getIdVehiculo().isEmpty())
                    ? vehiculo.getIdVehiculo() : UUID.randomUUID().toString();

            ps.setString(1, idVehiculo);
            ps.setString(2, vehiculo.getIdCliente());
            ps.setString(3, vehiculo.getMarca());
            ps.setString(4, vehiculo.getModelo());
            ps.setInt(5, vehiculo.getAnio());
            ps.setString(6, vehiculo.getPlaca());
            ps.setInt(7, vehiculo.getKilometraje());
            ps.setString(8, vehiculo.getEstado() != null && !vehiculo.getEstado().isEmpty()
                    ? vehiculo.getEstado() : "Activo");

            return ps.executeUpdate() > 0;
        }
    }

    public Vehiculo buscarPorPlaca(String placa) throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM Vehiculo WHERE placa = ?";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public boolean actualizar(Vehiculo vehiculo) throws SQLException {
        String sql = "UPDATE Vehiculo SET id_cliente=?, marca=?, modelo=?, anio=?, placa=?, kilometraje=?, estado=? "
                + "WHERE id_vehiculo=?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, vehiculo.getIdCliente());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getAnio());
            ps.setString(5, vehiculo.getPlaca());
            ps.setInt(6, vehiculo.getKilometraje());
            ps.setString(7, vehiculo.getEstado());
            ps.setString(8, vehiculo.getIdVehiculo());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String idVehiculo) throws SQLException {
        try (Connection conn = ConnectionDb.getConnection()) {
            // Un vehículo puede tener órdenes de servicio asociadas; si no se
            // borran primero, el DELETE falla por la llave foránea (por eso
            // antes salía "Ocurrió un error al eliminar el vehículo").
            try (PreparedStatement psOrdenes = conn.prepareStatement(
                    "DELETE FROM Orden WHERE id_vehiculo = ?")) {
                psOrdenes.setString(1, idVehiculo);
                psOrdenes.executeUpdate();
            }
            try (PreparedStatement psVehiculo = conn.prepareStatement(
                    "DELETE FROM Vehiculo WHERE id_vehiculo = ?")) {
                psVehiculo.setString(1, idVehiculo);
                return psVehiculo.executeUpdate() > 0;
            }
        }
    }
}
