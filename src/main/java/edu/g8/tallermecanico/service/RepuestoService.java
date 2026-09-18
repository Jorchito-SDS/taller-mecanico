feature/TMM-19-consultar-stock-bajo
package main.java.edu.g8.tallermecanico.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import main.java.edu.g8.tallermecanico.model.Repuesto;
import main.java.edu.g8.tallermecanico.repository.RepuestoRepository;

public class RepuestoService {

    private final RepuestoRepository repuestoRepository = new RepuestoRepository();

    public boolean registrarRepuesto(Repuesto repuesto, Connection conn) throws SQLException, IllegalArgumentException {
        if (repuesto.getNombre() == null || repuesto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del repuesto no puede estar vacío.");
        }
        if (repuesto.getStock() < 0 || repuesto.getStockMinimo() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        if (repuesto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0.");
        }
        return repuestoRepository.guardar(repuesto, conn);
    }

    public boolean descontarRepuestoDeOrden(int idRepuesto, int cantidad, Connection conn) throws SQLException, IllegalArgumentException {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a 0.");
        }
        boolean descontado = repuestoRepository.descontarStock(idRepuesto, cantidad, conn);
        if (!descontado) {
            throw new IllegalArgumentException("No hay suficiente stock disponible.");
        }
        return true;
    }

    public List<Repuesto> listarRepuestosStockBajo(Connection conn) throws SQLException {
        return repuestoRepository.obtenerStockBajo(conn);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.edu.g8.tallermecanico.service;

/**
 *
 * @author Mattt
 */
public class RepuestoService {
    
}
 main
