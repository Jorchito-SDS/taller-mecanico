package main.java.edu.g8.tallermecanico.util;


public class SesionUsuario {

    private final Rol rol;
    private final String nombre;
    private final String idReferencia; 

    public SesionUsuario(Rol rol, String nombre, String idReferencia) {
        this.rol = rol;
        this.nombre = nombre;
        this.idReferencia = idReferencia;
    }

    public Rol getRol() {
        return rol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdReferencia() {
        return idReferencia;
    }
}
