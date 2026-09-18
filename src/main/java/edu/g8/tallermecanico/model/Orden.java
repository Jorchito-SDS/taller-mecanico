package main.java.edu.g8.tallermecanico.model;

public class Orden {
    private String idOrden;
    private String idVehiculo;
    private String idMecanico;
    private String fechaRecepcion; // formato 'yyyy-MM-dd HH:mm:ss'
    private String fechaEntrega;
    private String estado; // Recibido, Diagnostico, En_reparacion, Listo, Entregado
    private String diagnostico;

    // Campos extra de solo lectura, útiles para mostrar en tablas (JOIN)
    private String placaVehiculo;
    private String nombreCliente;
    private String nombreMecanico;

    public Orden() {}

    public Orden(String idOrden, String idVehiculo, String idMecanico,
                 String fechaRecepcion, String fechaEntrega,
                 String estado, String diagnostico) {
        this.idOrden = idOrden;
        this.idVehiculo = idVehiculo;
        this.idMecanico = idMecanico;
        this.fechaRecepcion = fechaRecepcion;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.diagnostico = diagnostico;
    }

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

    public String getPlacaVehiculo() { return placaVehiculo; }
    public void setPlacaVehiculo(String placaVehiculo) { this.placaVehiculo = placaVehiculo; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public String getNombreMecanico() { return nombreMecanico; }
    public void setNombreMecanico(String nombreMecanico) { this.nombreMecanico = nombreMecanico; }
}