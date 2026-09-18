package main.java.edu.g8.tallermecanico.repository;

import main.java.edu.g8.tallermecanico.model.Repuesto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RepuestoRepository {

    public boolean guardar(Repuesto repuesto, Connection conn) throws SQLException {
        String sql = "INSERT INTO Repuesto (nombre, stock, stock_minimo, precio, proveedor) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, repuesto.getNombre());
            stmt.setInt(2, repuesto.getStock());
            stmt.setInt(3, repuesto.getStockMinimo());
            stmt.setDouble(4, repuesto.getPrecio());
            stmt.setString(5, repuesto.getProveedor());
            return stmt.executeUpdate() > 0;
        }
    }
    public boolean descontarStock(int idRepuesto, int cantidad, Connection conn) throws SQLException {
    String sql = "UPDATE Repuesto SET stock = stock - ? WHERE id_repuesto = ? AND stock >= ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, cantidad);
        stmt.setInt(2, idRepuesto);
        stmt.setInt(3, cantidad);
        return stmt.executeUpdate() > 0;
    }
}
}