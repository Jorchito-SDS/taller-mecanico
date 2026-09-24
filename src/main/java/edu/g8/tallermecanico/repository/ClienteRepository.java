package main.java.edu.g8.tallermecanico.repository;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.g8.tallermecanico.config.ConnectionDb;
import main.java.edu.g8.tallermecanico.model.Cliente;

public class ClienteRepository {

    public List<Cliente> buscarPorNombreOPlaca(String filtro) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT DISTINCT c.* FROM cliente c " +
                     "LEFT JOIN vehiculo v ON c.id_cliente = v.id_cliente " +
                     "WHERE c.nombre LIKE ? OR v.placa LIKE ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String wildcard = "%" + filtro + "%";
            stmt.setString(1, wildcard);
            stmt.setString(2, wildcard);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(rs.getInt("id_cliente"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setDireccion(rs.getString("direccion"));
                    clientes.add(cliente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }
}
