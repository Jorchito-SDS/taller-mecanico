/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.g8.tallermecanico.repository;

import main.java.edu.g8.tallermecanico.model.Vehiculo;
import main.java.edu.g8.tallermecanico.config.ConnectionDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author informatica
 */
public class VehiculoRepository {
    
    public boolean guardar(Vehiculo vehiculo) {
        String sql = "INSERT INTO vehiculo (id_vehiculo, id_cliente, placa, marca, modelo, anio, kilometraje) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, vehiculo.getId_vehiculo());
            stmt.setInt(2, vehiculo.getId_cliente());
            stmt.setString(3, vehiculo.getPlaca());
            stmt.setString(4, vehiculo.getMarca());
            stmt.setString(5, vehiculo.getModelo());
            stmt.setInt(6, vehiculo.getAnio());
            stmt.setInt(7, vehiculo.getKilometraje());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Vehiculo> listar() {
        List<Vehiculo> listaVehiculos = new ArrayList<>();
        String sql = "SELECT * FROM vehiculo";
        
        try (Connection conn = ConnectionDb.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setId_vehiculo(rs.getInt("id_vehiculo"));
                vehiculo.setId_cliente(rs.getInt("id_cliente"));
                vehiculo.setPlaca(rs.getString("placa"));
                vehiculo.setMarca(rs.getString("marca"));
                vehiculo.setModelo(rs.getString("modelo"));
                vehiculo.setAnio(rs.getInt("anio"));
                vehiculo.setKilometraje(rs.getInt("kilometraje"));
                
                listaVehiculos.add(vehiculo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return listaVehiculos;
    }

    public Vehiculo buscarPorPlaca(String placa) {
        String sql = "SELECT * FROM vehiculo WHERE placa = ?";
        Vehiculo vehiculo = null;
        
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, placa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    vehiculo = new Vehiculo();
                    vehiculo.setId_vehiculo(rs.getInt("id_vehiculo"));
                    vehiculo.setId_cliente(rs.getInt("id_cliente"));
                    vehiculo.setPlaca(rs.getString("placa"));
                    vehiculo.setMarca(rs.getString("marca"));
                    vehiculo.setModelo(rs.getString("modelo"));
                    vehiculo.setAnio(rs.getInt("anio"));
                    vehiculo.setKilometraje(rs.getInt("kilometraje"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return vehiculo;
    }
    
    public boolean actualizar(Vehiculo vehiculo) {
        String sql = "UPDATE vehiculo SET id_cliente = ?, placa = ?, marca = ?, modelo = ?, anio = ?, kilometraje = ? WHERE id_vehiculo = ?";
        
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, vehiculo.getId_cliente());
            stmt.setString(2, vehiculo.getPlaca());
            stmt.setString(3, vehiculo.getMarca());
            stmt.setString(4, vehiculo.getModelo());
            stmt.setInt(5, vehiculo.getAnio());
            stmt.setInt(6, vehiculo.getKilometraje());
            stmt.setInt(7, vehiculo.getId_vehiculo());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(String idVehiculo) {
        String sql = "DELETE FROM vehiculo WHERE id_vehiculo = ?";
        
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, idVehiculo);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}