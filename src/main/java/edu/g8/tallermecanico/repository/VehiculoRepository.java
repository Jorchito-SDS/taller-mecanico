package main.java.edu.g8.tallermecanico.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.g8.tallermecanico.config.ConnectionDb;
import main.java.edu.g8.tallermecanico.model.Vehiculo;

public class VehiculoRepository {

    public List<Vehiculo> listar() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT id_vehiculo, id_cliente, marca, modelo, anio, placa FROM Vehiculo";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vehiculo vehiculo = new Vehiculo(
                    rs.getString("id_vehiculo"),
                    rs.getString("id_cliente"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getInt("anio"),
                    rs.getString("placa")
                );
                lista.add(vehiculo);
            }
        }
        return lista;
    }

    public boolean guardar(Vehiculo vehiculo) throws SQLException {
        String sql = "INSERT INTO Vehiculo (id_cliente, marca, modelo, anio, placa) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, vehiculo.getIdCliente());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getAnio());
            ps.setString(5, vehiculo.getPlaca());

            return ps.executeUpdate() > 0;
        }
    }

    public Vehiculo buscarPorPlaca(String placa) throws SQLException {
        String sql = "SELECT id_vehiculo, id_cliente, marca, modelo, anio, placa FROM Vehiculo WHERE placa = ?";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Vehiculo(
                        rs.getString("id_vehiculo"),
                        rs.getString("id_cliente"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getInt("anio"),
                        rs.getString("placa")
                    );
                }
            }
        }
        return null;
    }

    public boolean actualizar(Vehiculo vehiculo) throws SQLException {
        String sql = "UPDATE Vehiculo SET id_cliente=?, marca=?, modelo=?, anio=?, placa=? WHERE id_vehiculo=?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, vehiculo.getIdCliente());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getAnio());
            ps.setString(5, vehiculo.getPlaca());
            ps.setString(6, vehiculo.getIdVehiculo());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String idVehiculo) throws SQLException {
        String sql = "DELETE FROM Vehiculo WHERE id_vehiculo = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, idVehiculo);
            return ps.executeUpdate() > 0;
        }
    }
}