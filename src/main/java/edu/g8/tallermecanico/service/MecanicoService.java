package main.java.edu.g8.tallermecanico.service;

import java.util.List;
import main.java.edu.g8.tallermecanico.security.jbcrypt.BCrypt;
import main.java.edu.g8.tallermecanico.model.Mecanico;
import main.java.edu.g8.tallermecanico.repository.MecanicoRepository;

public class MecanicoService {

    private final MecanicoRepository mecanicoRepository = new MecanicoRepository();

    public List<Mecanico> listarMecanicos() {
        return mecanicoRepository.listarMecanicos();
    }

    /**
     * Valida los datos del mecánico, hashea la contraseña y lo guarda.
     * @param passwordPlano contraseña en texto plano ingresada en el formulario
     *                      (nunca se guarda así, se hashea aquí antes de persistir)
     */
    public boolean registrarMecanico(Mecanico mecanico, String passwordPlano) {
        validarMecanico(mecanico);
        validarPassword(passwordPlano);

        String hash = BCrypt.hashpw(passwordPlano, BCrypt.gensalt());
        mecanico.setContrasenaHash(hash);

        return mecanicoRepository.guardar(mecanico);
    }

    private void validarMecanico(Mecanico mecanico) {
        if (mecanico.getNombre() == null || mecanico.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del mecánico es obligatorio.");
        }
        if (mecanico.getNombre().trim().length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres.");
        }
        if (mecanico.getEspecialidad() == null || mecanico.getEspecialidad().trim().isEmpty()) {
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        }
        if (mecanico.getTelefono() == null || !mecanico.getTelefono().matches("^[0-9\\-]{7,15}$")) {
            throw new IllegalArgumentException("El teléfono no tiene un formato válido.");
        }
    }

    private void validarPassword(String password) {
        if (password == null || password.trim().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
    }
}