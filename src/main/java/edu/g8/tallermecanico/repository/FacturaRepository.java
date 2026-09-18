package main.java.edu.g8.tallermecanico.repository;

import main.java.edu.g8.tallermecanico.model.Factura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FacturaRepository {

    public boolean generarFactura(Factura factura, Connection conn) throws SQLException {
        String sql = "INSERT INTO Factura (id_orden, total) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, factura.getIdOrden());
            stmt.setDouble(2, factura.getTotal());
            return stmt.executeUpdate() > 0;
        }
    }
}