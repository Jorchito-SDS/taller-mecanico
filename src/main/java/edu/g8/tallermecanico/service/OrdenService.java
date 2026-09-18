package main.java.edu.g8.tallermecanico.service;

import java.util.List;
import main.java.edu.g8.tallermecanico.model.Orden;
import main.java.edu.g8.tallermecanico.repository.OrdenRepository;
import main.java.edu.g8.tallermecanico.repository.VehiculoRepository;

public class OrdenService {

    private final OrdenRepository ordenRepository = new OrdenRepository();
    private final VehiculoRepository vehiculoRepository = new VehiculoRepository();

    // Flujo válido de estados: no se puede saltar pasos ni retroceder
    private static final List<String> FLUJO_ESTADOS = List.of(
            "Recibido", "Diagnostico", "En_reparacion", "Listo", "Entregado"
    );

    public List<Orden> listarActivas() {
        return ordenRepository.listarActivas();
    }

    public List<Orden> listarPorMecanico(String idMecanico) {
        if (idMecanico == null || idMecanico.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar un mecánico válido.");
        }
        return ordenRepository.listarPorMecanico(idMecanico);
    }

    /**
     * Crea una nueva orden en estado inicial "Recibido", a partir de la placa
     * del vehículo ingresada por el recepcionista (no requiere el ID interno).
     */
    public boolean crearOrdenPorPlaca(String placa, String diagnostico) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar la placa del vehículo.");
        }

        String idVehiculo = vehiculoRepository.buscarIdPorPlaca(placa);
        if (idVehiculo == null) {
            throw new IllegalArgumentException(
                "No existe ningún vehículo registrado con la placa '" + placa.trim().toUpperCase()
                + "'. Verifique la placa o registre primero el vehículo."
            );
        }

        Orden orden = new Orden();
        orden.setIdVehiculo(idVehiculo);
        orden.setDiagnostico(diagnostico);
        orden.setEstado("Recibido");

        return ordenRepository.guardar(orden);
    }

    /** Cambia el estado de la orden, validando que el nuevo estado sea el siguiente en el flujo. */
    public boolean actualizarEstado(Orden ordenActual, String nuevoEstado) {
        if (!FLUJO_ESTADOS.contains(nuevoEstado)) {
            throw new IllegalArgumentException("Estado no reconocido: " + nuevoEstado);
        }
        int indiceActual = FLUJO_ESTADOS.indexOf(ordenActual.getEstado());
        int indiceNuevo = FLUJO_ESTADOS.indexOf(nuevoEstado);

        if (indiceNuevo < indiceActual) {
            throw new IllegalArgumentException("No se puede retroceder el estado de una orden.");
        }
        return ordenRepository.actualizarEstado(ordenActual.getIdOrden(), nuevoEstado);
    }

    public boolean asignarMecanico(String idOrden, String idMecanico) {
        if (idOrden == null || idMecanico == null) {
            throw new IllegalArgumentException("Orden y mecánico son obligatorios para la asignación.");
        }
        return ordenRepository.asignarMecanico(idOrden, idMecanico);
    }
}