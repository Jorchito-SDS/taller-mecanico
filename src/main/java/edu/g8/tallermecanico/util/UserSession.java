package main.java.edu.g8.tallermecanico.util;

public class UserSession {
    private static UserSession instance;
    private int idUsuario;
    private String nombre;
    private String rol;
    private Integer idReferencia;

    private UserSession(int idUsuario, String nombre, String rol, Integer idReferencia) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.rol = rol;
        this.idReferencia = idReferencia;
    }

    public static void iniciarSesion(int idUsuario, String nombre, String rol, Integer idReferencia) {
        instance = new UserSession(idUsuario, nombre, rol, idReferencia);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public static void cerrarSesion() {
        instance = null;
    }

    public String getRol() { return rol; }
    public Integer getIdReferencia() { return idReferencia; }
    public String getNombre() { return nombre; }
}
