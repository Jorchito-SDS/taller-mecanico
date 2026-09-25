package main.java.edu.g8.tallermecanico.model;

public class Orden {
    private String idOrden; // VARCHAR(36) UUID
    private String idVehiculo;
    private String idMecanico;
    private String fechaRecepcion;
    private String fechaEntrega;
    private String estado;
    private String diagnostico;

    // Campos auxiliares provenientes de los JOINs para mostrar en las vistas
    private String cliente;
    private String placa;
    private String mecanico;

    public Orden() {}

    // Constructor para consultas generales con JOIN
    public Orden(String idOrden, String placa, String cliente, String mecanico, String estado, String fechaRecepcion) {
        this.idOrden = idOrden;
        this.placa = placa;
        this.cliente = cliente;
        this.mecanico = mecanico;
        this.estado = estado;
        this.fechaRecepcion = fechaRecepcion;
    }

    // Getters y Setters de los campos de la tabla
    public String getIdOrden() { return idOrden; }
    public void setIdOrden(String idOrden) { this.idOrden = idOrden; }

    public String getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(String idVehiculo) { this.idVehiculo = idVehiculo; }

    public String getIdMecanico() { return idMecanico; }
    public void setIdMecanico(String idMecanico) { this.idMecanico = idMecanico; }

    public String getFechaRecepcion() { return fechaRecepcion; }
    public void setFechaRecepcion(String fechaRecepcion) { this.fechaRecepcion = fechaRecepcion; }

    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    // Getters y Setters de campos de apoyo (JOIN)
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getMecanico() { return mecanico; }
    public void setMecanico(String mecanico) { this.mecanico = mecanico; }

    @Override
    public String toString() {
        return "Orden " + idOrden.substring(0, 8) + "... (" + (placa != null ? placa : "Sin Placa") + ")";
    }
}