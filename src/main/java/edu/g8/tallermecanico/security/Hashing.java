package main.java.edu.g8.tallermecanico.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import main.java.edu.g8.tallermecanico.security.jbcrypt.BCrypt;

/**
 * Utilidad central para crear y verificar contraseñas.
 *
 * Las contraseñas nuevas se guardan con BCrypt (hashPassword). Para no romper
 * registros antiguos que se crearon con la función SQL SHA2(?, 256) del
 * MecanicoRepository original, matches(...) también reconoce un hash SHA-256
 * en hexadecimal (64 caracteres) y lo valida en Java.
 */
public final class Hashing {

    private Hashing() {
    }

    public static String hashPassword(String plano) {
        return BCrypt.hashpw(plano, BCrypt.gensalt());
    }

    public static boolean matches(String plano, String hashGuardado) {
        if (plano == null || hashGuardado == null || hashGuardado.isBlank()) {
            return false;
        }
        if (esHashBCrypt(hashGuardado)) {
            try {
                return BCrypt.checkpw(plano, hashGuardado);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        if (esHashSha256Hex(hashGuardado)) {
            return sha256Hex(plano).equalsIgnoreCase(hashGuardado);
        }
        // Último recurso: comparación literal (por si el dato se guardó en texto plano)
        return plano.equals(hashGuardado);
    }

    private static boolean esHashBCrypt(String hash) {
        return hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$");
    }

    private static boolean esHashSha256Hex(String hash) {
        return hash.length() == 64 && hash.matches("[0-9a-fA-F]+");
    }

    private static String sha256Hex(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(texto.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible en esta JVM", e);
        }
    }
}
