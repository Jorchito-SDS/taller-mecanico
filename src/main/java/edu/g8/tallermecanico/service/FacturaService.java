package main.java.edu.g8.tallermecanico.service;

import java.sql.Connection;
import java.sql.SQLException;
import main.java.edu.g8.tallermecanico.model.Factura;
import main.java.edu.g8.tallermecanico.repository.FacturaRepository;

public class FacturaService {

    private final FacturaRepository facturaRepository = new FacturaRepository();

    public boolean procesarFactura(Factura factura, Connection conn) throws SQLException, IllegalArgumentException {
        if (factura.getIdOrden() <= 0) {
            throw new IllegalArgumentException("Debe asociar una orden válida a la factura.");
        }
        if (factura.getTotal() <= 0) {
            throw new IllegalArgumentException("El total de la factura debe ser mayor a 0.");
        }
        return facturaRepository.generarFactura(factura, conn);
    }
}