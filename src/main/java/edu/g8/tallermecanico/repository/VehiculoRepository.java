package main.java.edu.g8.tallermecanico.repository;

import java.sql.*;
import main.java.edu.g8.tallermecanico.config.ConnectionDb;

public class VehiculoRepository {

    /**
     * Busca el id_vehiculo (UUID) a partir de la placa.
     * @return el id del vehículo, o null si no existe ninguno con esa placa.
     */
    public String buscarIdPorPlaca(String placa) {
        String sql = "SELECT id_vehiculo FROM Vehiculo WHERE placa = ?";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa.trim().toUpperCase());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id_vehiculo");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** Verifica si existe un vehículo con esa placa (útil para validar antes de crear la orden). */
    public boolean existePlaca(String placa) {
        return buscarIdPorPlaca(placa) != null;
    }
}
