package main.java.edu.g8.tallermecanico.model;

public class Repuesto {
    private int idRepuesto;
    private String nombre;
    private int stock;
    private int stockMinimo;
    private double precio;
    private String proveedor;

    public Repuesto() {}

    public Repuesto(int idRepuesto, String nombre, int stock, int stockMinimo, double precio, String proveedor) {
        this.idRepuesto = idRepuesto;
        this.nombre = nombre;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.precio = precio;
        this.proveedor = proveedor;
    }

    public int getIdRepuesto() { return idRepuesto; }
    public void setIdRepuesto(int idRepuesto) { this.idRepuesto = idRepuesto; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }
}