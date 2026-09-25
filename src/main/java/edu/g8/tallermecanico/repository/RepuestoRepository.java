package main.java.edu.g8.tallermecanico.repository;

import main.java.edu.g8.tallermecanico.model.Repuesto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RepuestoRepository {

    private Repuesto mapear(ResultSet rs) throws SQLException {
        Repuesto r = new Repuesto();
        r.setIdRepuesto(rs.getString("id_repuesto"));
        r.setNombre(rs.getString("nombre"));
        r.setStock(rs.getInt("stock"));
        r.setStockMinimo(rs.getInt("stock_minimo"));
        r.setPrecio(rs.getDouble("precio"));
        r.setProveedor(rs.getString("proveedor"));
        return r;
    }

    public boolean guardar(Repuesto repuesto, Connection conn) throws SQLException {
        String sql = "INSERT INTO Repuesto (id_repuesto, nombre, stock, stock_minimo, precio, proveedor) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String id = (repuesto.getIdRepuesto() != null && !repuesto.getIdRepuesto().isEmpty())
                    ? repuesto.getIdRepuesto() : UUID.randomUUID().toString();
            repuesto.setIdRepuesto(id);

            stmt.setString(1, id);
            stmt.setString(2, repuesto.getNombre());
            stmt.setInt(3, repuesto.getStock());
            stmt.setInt(4, repuesto.getStockMinimo());
            stmt.setDouble(5, repuesto.getPrecio());
            stmt.setString(6, repuesto.getProveedor());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean actualizar(Repuesto repuesto, Connection conn) throws SQLException {
        String sql = "UPDATE Repuesto SET nombre = ?, stock = ?, stock_minimo = ?, precio = ?, proveedor = ? WHERE id_repuesto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, repuesto.getNombre());
            stmt.setInt(2, repuesto.getStock());
            stmt.setInt(3, repuesto.getStockMinimo());
            stmt.setDouble(4, repuesto.getPrecio());
            stmt.setString(5, repuesto.getProveedor());
            stmt.setString(6, repuesto.getIdRepuesto());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String idRepuesto, Connection conn) throws SQLException {
        String sql = "DELETE FROM Repuesto WHERE id_repuesto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idRepuesto);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Repuesto> listarTodos(Connection conn) throws SQLException {
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Repuesto ORDER BY nombre";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Repuesto> buscarPorNombreLike(String filtro, Connection conn) throws SQLException {
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Repuesto WHERE nombre LIKE ? ORDER BY nombre";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + filtro + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public boolean descontarStock(String idRepuesto, int cantidad, Connection conn) throws SQLException {
        String sql = "UPDATE Repuesto SET stock = stock - ? WHERE id_repuesto = ? AND stock >= ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cantidad);
            stmt.setString(2, idRepuesto);
            stmt.setInt(3, cantidad);
            return stmt.executeUpdate() > 0;
        }
    }

    // Antes dependía de una vista "VistaStockBajo" que no existe en la base de
    // datos (causaba "Table ...vistastockbajo doesn't exist"). Se reemplaza
    // por una consulta directa sobre la tabla Repuesto, que siempre funciona.
    public List<Repuesto> obtenerStockBajo(Connection conn) throws SQLException {
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Repuesto WHERE stock <= stock_minimo ORDER BY nombre";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }
}
