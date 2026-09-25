package main.java.edu.g8.tallermecanico.model;

public class Mecanico {
    private String idMecanico; // VARCHAR(36) UUID
    private String nombre;
    private String especialidad;
    private String telefono;
    private int disponible; // 1 = Disponible, 0 = No disponible
    private String contrasenaHash;

    public Mecanico() {}

    public Mecanico(String idMecanico, String nombre, String especialidad, String telefono, int disponible) {
        this.idMecanico = idMecanico;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.disponible = disponible;
    }

    public Mecanico(String nombre, String especialidad, String telefono, int disponible, String contrasenaHash) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.disponible = disponible;
        this.contrasenaHash = contrasenaHash;
    }

    // Getters y Setters
    public String getIdMecanico() { return idMecanico; }
    public void setIdMecanico(String idMecanico) { this.idMecanico = idMecanico; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public int getDisponible() { return disponible; }
    public void setDisponible(int disponible) { this.disponible = disponible; }

    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    @Override
    public String toString() {
        return nombre + " (" + (especialidad != null ? especialidad : "General") + ")";
    }
}