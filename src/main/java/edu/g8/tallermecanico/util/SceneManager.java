package main.java.edu.g8.tallermecanico.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.edu.g8.tallermecanico.controller.MenuController;

public class SceneManager {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void cambiarVista(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            primaryStage.setTitle(titulo);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cambiarVistaConRol(String fxmlPath, String titulo, String rol) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Si la vista de destino es el menú principal, configuramos las opciones visibles según el rol
            Object controller = loader.getController();
            if (controller instanceof MenuController) {
                ((MenuController) controller).configurarMenuPorRol(rol);
            }

            primaryStage.setTitle(titulo + " - " + rol);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}