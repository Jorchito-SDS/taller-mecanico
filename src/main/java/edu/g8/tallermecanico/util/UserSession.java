package main.java.edu.g8.tallermecanico.util;

public class UserSession {

    private static UserSession instance;

    private String idUsuario;
    private String nombre;
    private String rol;
    private Object idReferencia; // Almacena el ID de referencia del mecánico o cliente

    // Constructor privado para evitar instanciación directa
    private UserSession() {}

    // Obtiene la instancia única de la sesión (Singleton)
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Método para registrar los datos del usuario al iniciar sesión
    public void iniciarSesion(String idUsuario, String nombre, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.rol = rol;
    }

    // Sobrecarga para iniciar sesión incluyendo idReferencia
    public void iniciarSesion(String idUsuario, String nombre, String rol, Object idReferencia) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.rol = rol;
        this.idReferencia = idReferencia;
    }

    // Método para limpiar y destruir la sesión activa
    public void limpiarSesion() {
        this.idUsuario = null;
        this.nombre = null;
        this.rol = null;
        this.idReferencia = null;
        instance = null;
    }

    // Getters y Setters
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Object getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(Object idReferencia) {
        this.idReferencia = idReferencia;
    }
}