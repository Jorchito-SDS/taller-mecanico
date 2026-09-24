package main.java.edu.g8.tallermecanico.model;

public class Vehiculo {
    private String idVehiculo;
    private String idCliente;
    private String marca;
    private String modelo;
    private int anio;
    private String placa;

    public Vehiculo() {
    }

    public Vehiculo(String idVehiculo, String idCliente, String marca, String modelo, int anio, String placa) {
        this.idVehiculo = idVehiculo;
        this.idCliente = idCliente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.placa = placa;
    }

    public Vehiculo(String idCliente, String marca, String modelo, int anio, String placa) {
        this.idCliente = idCliente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.placa = placa;
    }

    public String getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(String idVehiculo) { this.idVehiculo = idVehiculo; }

    public String getIdCliente() { return idCliente; }
    public void setIdCliente(String idCliente) { this.idCliente = idCliente; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
}