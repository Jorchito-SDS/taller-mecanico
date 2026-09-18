package main.java.edu.g8.tallermecanico.model;

import java.sql.Timestamp;

public class Factura {
    private int idFactura;
    private int idOrden;
    private Timestamp fechaEmision;
    double total;

    public Factura() {}

    public Factura(int idFactura, int idOrden, Timestamp fechaEmision, double total) {
        this.idFactura = idFactura;
        this.idOrden = idOrden;
        this.fechaEmision = fechaEmision;
        this.total = total;
    }

    public int getIdFactura() { return idFactura; }
    public void setIdFactura(int idFactura) { this.idFactura = idFactura; }

    public int getIdOrden() { return idOrden; }
    public void setIdOrden(int idOrden) { this.idOrden = idOrden; }

    public Timestamp getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(Timestamp fechaEmision) { this.fechaEmision = fechaEmision; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}