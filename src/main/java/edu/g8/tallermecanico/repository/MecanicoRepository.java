/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.g8.tallermecanico.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.edu.g8.tallermecanico.config.ConnectionDb;
import main.java.edu.g8.tallermecanico.model.Mecanico;

public class MecanicoRepository {

    public List<Mecanico> listarMecanicos() {
        List<Mecanico> mecanicos = new ArrayList<>();
        String sql = "SELECT * FROM Mecanico";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                mecanicos.add(new Mecanico(
                    rs.getString("id_mecanico"),
                    rs.getString("nombre"),
                    rs.getString("especialidad"),
                    rs.getString("telefono"),
                    rs.getInt("disponible"),
                    rs.getString("contrasena_hash")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mecanicos;
    }

    public boolean guardar(Mecanico mecanico) {
        String sql = "INSERT INTO Mecanico (nombre, especialidad, telefono, disponible, contrasena_hash) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, mecanico.getNombre());
            stmt.setString(2, mecanico.getEspecialidad());
            stmt.setString(3, mecanico.getTelefono());
            stmt.setInt(4, mecanico.getDisponible());
            stmt.setString(5, mecanico.getContrasenaHash());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

