package main.java.edu.g8.tallermecanico;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.edu.g8.tallermecanico.util.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setMinWidth(900);
        stage.setMinHeight(620);

        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.showLoginView();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
