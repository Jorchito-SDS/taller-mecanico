
package main.java.edu.g8.tallermecanico;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Reemplaza "VehiculoView.fxml" por "ClienteView.fxml" si quieres probar la otra vista primero.
            // Asegúrate de que la ruta del recurso coincida con la ubicación de tu archivo FXML en el proyecto.
           URL fxmlUrl = getClass().getResource("/view/VehiculoView.fxml");
            
            if (fxmlUrl == null) {
                System.out.println("No se pudo encontrar el archivo FXML. Revisa la ruta.");
                return;
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            
            primaryStage.setTitle("Sistema de Taller Mecánico - Módulo de Vehículos");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}


