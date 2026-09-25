package main.java.edu.g8.tallermecanico.util;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage primaryStage;

    /**
     * Guarda la referencia del Stage principal otorgado por JavaFX.
     */
    public static void inicializar(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Cambia la escena actual cargando una nueva vista FXML.
     * 
     * @param fxmlPath Ruta al archivo .fxml (ej. "/view/LoginView.fxml")
     * @param titulo   Título de la ventana
     */
    public static void cambiarVista(String fxmlPath, String titulo) {
        if (primaryStage == null) {
            System.err.println("Error: SceneManager no ha sido inicializado con un Stage.");
            return;
        }

        try {
            URL resource = SceneManager.class.getResource(fxmlPath);
            if (resource == null) {
                System.err.println("❌ ERROR: No se encontró el archivo FXML en la ruta: " + fxmlPath);
                System.err.println("Verifica que el archivo esté en 'src/resources" + fxmlPath + "' o 'src" + fxmlPath + "'");
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            primaryStage.setTitle(titulo);
            
            // Si ya hay una escena configurada, reutilizamos el tamaño
            if (primaryStage.getScene() != null) {
                primaryStage.getScene().setRoot(root);
            } else {
                primaryStage.setScene(new Scene(root, 800, 600));
            }

            primaryStage.show();

        } catch (IOException e) {
            System.err.println("❌ ERROR al cargar la vista FXML: " + fxmlPath);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ ERROR inesperado durante la inicialización de la vista:");
            e.printStackTrace();
        }
    }

    /**
     * Permite obtener el Stage si se necesita manipular la ventana directamente.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}