package main.java.edu.g8.tallermecanico.config;

import main.java.edu.g8.tallermecanico.security.Hashing;

/**
 * Credenciales del perfil Gerente/Administrador.
 *
 * El proyecto no tiene una tabla de usuarios de gerencia en la base de datos,
 * así que -de momento- se valida contra un usuario fijo definido aquí. Para
 * cambiar la contraseña del gerente basta con reemplazar HASH_PASSWORD por el
 * resultado de Hashing.hashPassword("nuevaContraseña") impreso una vez por
 * consola (ver método main de esta clase).
 */
public final class AdminCredentials {

    public static final String USUARIO = "gerente";

    // Contraseña por defecto: "taller2026"  (cámbiala generando un hash nuevo)
    public static final String HASH_PASSWORD =
            "$2a$10$bn2QY8zhMQABS41yGoUDp.KjRMAczTH.GiHaCDftK.cWRfQ2BV3PS";

    private AdminCredentials() {
    }

    public static boolean autenticar(String usuario, String contrasena) {
        return USUARIO.equalsIgnoreCase(usuario) && Hashing.matches(contrasena, HASH_PASSWORD);
    }

    /** Genera un hash BCrypt nuevo; útil para definir otra contraseña de gerente. */
    public static void main(String[] args) {
        String pass = args.length > 0 ? args[0] : "taller2026";
        System.out.println(Hashing.hashPassword(pass));
    }
}
