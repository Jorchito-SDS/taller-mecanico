
package main.java.edu.g8.tallermecanico;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author informatica
 */
public class Main extends Application {

@Override
    public void start(Stage primaryStage) {
        try {
            // Buscamos el FXML dentro de la carpeta resources/view/
            Parent root = FXMLLoader.load(getClass().getResource("/view/ClienteView.fxml"));
            
            Scene scene = new Scene(root);
            
            primaryStage.setTitle("Taller Mecánico - Registro de Clientes");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}


