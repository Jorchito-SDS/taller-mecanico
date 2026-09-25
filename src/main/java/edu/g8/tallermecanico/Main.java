package main.java.edu.g8.tallermecanico;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Guardamos la ventana principal
        SceneManager.inicializar(stage);

        // Dimensiones mínimas de la aplicación
        stage.setMinWidth(800);
        stage.setMinHeight(600);

SceneManager.cambiarVista("/view/LoginView.fxml", "Acceso - Taller Mecánico");
    }

    public static void main(String[] args) {
        launch(args);
    }
}