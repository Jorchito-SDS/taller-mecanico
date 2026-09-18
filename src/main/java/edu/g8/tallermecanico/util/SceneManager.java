package main.java.edu.g8.tallermecanico.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controla el cambio de vistas dentro de la ventana principal (Stage).
 * Se inicializa una sola vez desde Main, y cualquier controlador
 * puede usarla para navegar a otra pantalla.
 */
public class SceneManager {

    private static Stage stagePrincipal;

    private SceneManager() {
        // Clase de utilidad: no se instancia
    }

    /** Debe llamarse una única vez desde Main.start(). */
    public static void inicializar(Stage stage) {
        stagePrincipal = stage;
    }

    /**
     * Cambia la vista actual por la que indica la ruta del FXML.
     * @param rutaFxml ruta absoluta dentro de resources, ej: "/resources/view/OrdenView.fxml"
     * @param titulo   título que se mostrará en la ventana
     */
    public static void cambiarVista(String rutaFxml, String titulo) {
        try {
            Parent root = FXMLLoader.load(SceneManager.class.getResource(rutaFxml));
            Scene scene = new Scene(root);
            stagePrincipal.setScene(scene);
            stagePrincipal.setTitle(titulo);
            stagePrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo cargar la vista: " + rutaFxml, e);
        }
    }

    public static Stage getStagePrincipal() {
        return stagePrincipal;
    }
}