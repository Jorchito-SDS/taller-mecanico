package main.java.edu.g8.tallermecanico.util;

/**
 * Perfiles de acceso de la aplicación. Cada perfil tiene su propia pantalla de
 * inicio de sesión (mismo FXML, textos y color distintos).
 */
public enum Rol {

    CLIENTE("Cliente", "Portal de Clientes", "Correo electrónico", "correo@ejemplo.com",
            "role-cliente", "👤"),
    MECANICO("Mecánico", "Portal de Mecánicos", "Nombre del mecánico", "Nombre registrado en el taller",
            "role-mecanico", "🔧"),
    GERENTE("Gerente", "Panel de Gerencia", "Usuario", "Usuario de gerencia",
            "role-gerente", "💼");

    private final String etiqueta;
    private final String tituloLogin;
    private final String etiquetaUsuario;
    private final String promptUsuario;
    private final String cssClass;
    private final String icono;

    Rol(String etiqueta, String tituloLogin, String etiquetaUsuario, String promptUsuario,
        String cssClass, String icono) {
        this.etiqueta = etiqueta;
        this.tituloLogin = tituloLogin;
        this.etiquetaUsuario = etiquetaUsuario;
        this.promptUsuario = promptUsuario;
        this.cssClass = cssClass;
        this.icono = icono;
    }

    public String getEtiqueta() { return etiqueta; }
    public String getTituloLogin() { return tituloLogin; }
    public String getEtiquetaUsuario() { return etiquetaUsuario; }
    public String getPromptUsuario() { return promptUsuario; }
    public String getCssClass() { return cssClass; }
    public String getIcono() { return icono; }
}
