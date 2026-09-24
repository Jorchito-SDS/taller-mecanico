
package main.java.edu.g8.tallermecanico;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager.inicializar(stage);

        stage.setMinWidth(800);
        stage.setMinHeight(600);

        SceneManager.cambiarVista("/resources/view/MenuView.fxml", "Taller Mecánico Multimarca");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}


