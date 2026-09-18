package main.java.edu.g8.tallermecanico.repository;

import main.java.edu.g8.tallermecanico.model.Repuesto;
import java.sql.Connection;
import java.sql.PreparedStatement;
 feature/TMM-19-consultar-stock-bajo
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;
 main

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
feature/TMM-19-consultar-stock-bajo

    public boolean descontarStock(int idRepuesto, int cantidad, Connection conn) throws SQLException {
        String sql = "UPDATE Repuesto SET stock = stock - ? WHERE id_repuesto = ? AND stock >= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cantidad);
            stmt.setInt(2, idRepuesto);
            stmt.setInt(3, cantidad);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Repuesto> obtenerStockBajo(Connection conn) throws SQLException {
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM VistaStockBajo";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Repuesto r = new Repuesto();
                r.setIdRepuesto(rs.getInt("id_repuesto"));
                r.setNombre(rs.getString("nombre"));
                r.setStock(rs.getInt("stock"));
                r.setStockMinimo(rs.getInt("stock_minimo"));
                r.setPrecio(rs.getDouble("precio"));
                r.setProveedor(rs.getString("proveedor"));
                lista.add(r);
            }
        }
        return lista;
    }

 main
}