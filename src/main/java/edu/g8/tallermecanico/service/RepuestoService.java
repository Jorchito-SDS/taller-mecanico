package main.java.edu.g8.tallermecanico.service;

import java.sql.Connection;
import java.sql.SQLException;
import main.java.edu.g8.tallermecanico.repository.RepuestoRepository;

public class RepuestoService {

    private final RepuestoRepository repuestoRepository = new RepuestoRepository();

    public boolean descontarRepuestoDeOrden(int idRepuesto, int cantidad, Connection conn) throws SQLException, IllegalArgumentException {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a 0.");
        }
        boolean descontado = repuestoRepository.descontarStock(idRepuesto, cantidad, conn);
        if (!descontado) {
            throw new IllegalArgumentException("No hay suficiente stock disponible para descontar esta cantidad.");
        }
        return true;
    }
}