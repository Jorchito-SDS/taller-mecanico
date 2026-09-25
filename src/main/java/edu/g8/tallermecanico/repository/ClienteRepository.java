package main.java.edu.g8.tallermecanico.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import main.java.edu.g8.tallermecanico.config.ConnectionDb;
import main.java.edu.g8.tallermecanico.model.Cliente;

public class ClienteRepository {

    public List<Cliente> listar() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id_cliente, nombre, telefono, email, direccion FROM cliente";
        try (Connection conn = ConnectionDb.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getString("id_cliente"));
                c.setNombre(rs.getString("nombre"));
                c.setTelefono(rs.getString("telefono"));
                c.setEmail(rs.getString("email"));
                c.setDireccion(rs.getString("direccion"));
                lista.add(c);
            }
        }
        return lista;
    }

    public List<Cliente> buscarPorNombreOPlaca(String filtro) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT c.id_cliente, c.nombre, c.telefono, c.email, c.direccion " +
                     "FROM cliente c LEFT JOIN vehiculo v ON c.id_cliente = v.id_cliente " +
                     "WHERE c.nombre LIKE ? OR v.placa LIKE ?";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + filtro + "%");
            stmt.setString(2, "%" + filtro + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdCliente(rs.getString("id_cliente"));
                    c.setNombre(rs.getString("nombre"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setEmail(rs.getString("email"));
                    c.setDireccion(rs.getString("direccion"));
                    lista.add(c);
                }
            }
        }
        return lista;
    }

    public boolean guardar(Cliente cliente) throws SQLException {
        // Contraseña por defecto cuando se crea un cliente sin definirla
        // (por ejemplo desde el panel de Gerencia, que no pide contraseña).
        return guardar(cliente, "123456");
    }

    public boolean guardar(Cliente cliente, String password) throws SQLException {
        String sql = "INSERT INTO cliente (id_cliente, nombre, telefono, email, direccion, contrasena_hash) "
                    + "VALUES (?, ?, ?, ?, ?, SHA2(?, 256))";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String idCliente = (cliente.getIdCliente() != null && !cliente.getIdCliente().isEmpty())
                               ? cliente.getIdCliente()
                               : UUID.randomUUID().toString();

            stmt.setString(1, idCliente);
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getTelefono() != null ? cliente.getTelefono() : "00000000");
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getDireccion());
            stmt.setString(6, (password != null && !password.isEmpty()) ? password : "123456");

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nombre = ?, telefono = ?, email = ?, direccion = ? WHERE id_cliente = ?";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getTelefono());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getDireccion());
            stmt.setString(5, cliente.getIdCliente());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String idCliente) throws SQLException {
        try (Connection conn = ConnectionDb.getConnection()) {
            // Un cliente puede tener vehículos, y esos vehículos órdenes de
            // servicio asociadas. Hay que borrar en cascada manualmente
            // porque las llaves foráneas rechazan el DELETE directo (por eso
            // antes salía "Ocurrió un error al eliminar el cliente").
            try (PreparedStatement psOrdenes = conn.prepareStatement(
                    "DELETE o FROM Orden o JOIN Vehiculo v ON o.id_vehiculo = v.id_vehiculo WHERE v.id_cliente = ?")) {
                psOrdenes.setString(1, idCliente);
                psOrdenes.executeUpdate();
            }
            try (PreparedStatement psVehiculos = conn.prepareStatement(
                    "DELETE FROM Vehiculo WHERE id_cliente = ?")) {
                psVehiculos.setString(1, idCliente);
                psVehiculos.executeUpdate();
            }
            try (PreparedStatement psCliente = conn.prepareStatement(
                    "DELETE FROM cliente WHERE id_cliente = ?")) {
                psCliente.setString(1, idCliente);
                return psCliente.executeUpdate() > 0;
            }
        }
    }

    public Cliente buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT id_cliente, nombre, telefono, email, direccion FROM cliente WHERE email = ?";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdCliente(rs.getString("id_cliente"));
                    c.setNombre(rs.getString("nombre"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setEmail(rs.getString("email"));
                    c.setDireccion(rs.getString("direccion"));
                    return c;
                }
            }
        }
        return null;
    }

    public Cliente autenticar(String email, String password) throws SQLException {
        String sql = "SELECT id_cliente, nombre, telefono, email, direccion FROM cliente "
                    + "WHERE email = ? AND contrasena_hash = SHA2(?, 256)";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setIdCliente(rs.getString("id_cliente"));
                    c.setNombre(rs.getString("nombre"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setEmail(rs.getString("email"));
                    c.setDireccion(rs.getString("direccion"));
                    return c;
                }
            }
        }
        return null;
    }
}