package main.java.edu.g8.tallermecanico;

import javafx.application.Application;
import javafx.stage.Stage;

import main.java.edu.g8.tallermecanico.util.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager.inicializar(stage);

        stage.setMinWidth(800);
        stage.setMinHeight(600);

        // Pantalla inicial: menú principal
        SceneManager.cambiarVista("/resources/view/MenuView.fxml", "Taller Mecánico Multimarca");
    }

    public static void main(String[] args) {
        launch(args);
    }
}