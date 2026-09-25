package main.java.edu.g8.tallermecanico.util;

import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import main.java.edu.g8.tallermecanico.controller.AsignarMecanicoController;
import main.java.edu.g8.tallermecanico.controller.ClienteController;
import main.java.edu.g8.tallermecanico.controller.GestionClientesController;
import main.java.edu.g8.tallermecanico.controller.LoginController;
import main.java.edu.g8.tallermecanico.controller.MecanicoController;
import main.java.edu.g8.tallermecanico.controller.MenuController;
import main.java.edu.g8.tallermecanico.controller.OrdenController;
import main.java.edu.g8.tallermecanico.controller.OrdenesMecanicoController;
import main.java.edu.g8.tallermecanico.controller.RepuestoController;
import main.java.edu.g8.tallermecanico.controller.RolLoginController;
import main.java.edu.g8.tallermecanico.controller.VehiculoController;
import main.java.edu.g8.tallermecanico.service.ClienteService;
import main.java.edu.g8.tallermecanico.service.MecanicoService;
import main.java.edu.g8.tallermecanico.service.OrdenService;
import main.java.edu.g8.tallermecanico.service.RepuestoService;
import main.java.edu.g8.tallermecanico.service.VehiculoService;
import main.java.edu.g8.tallermecanico.repository.MecanicoRepository;
import main.java.edu.g8.tallermecanico.repository.OrdenRepository;

/**
 * Se crea una sola vez en Main y se pasa por constructor a cada controller
 * que necesite navegar. Cada showXView() arma su propio FXMLLoader con un
 * controllerFactory que construye el controller pasándole a mano sus
 * dependencias (services y, si aplica, la sesión del usuario logueado).
 */
public class SceneManager {

    private static final String CSS = "/css/Style.css";
    private static final String FXML_PATH = "/view/";
    private static final double ANCHO = 1000;
    private static final double ALTO = 680;

    private final Stage primaryStage;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showLoginView() throws Exception {
        cargar("LoginView.fxml", "Acceso - Taller Mecánico", clazz -> new LoginController(this));
    }

    public void showRolLoginView(Rol rol) throws Exception {
        cargar("RolLoginView.fxml", "Iniciar sesión - Taller Mecánico",
                clazz -> new RolLoginController(rol, new ClienteService(), new MecanicoService(), this));
    }

    public void showMenuGerenteView(SesionUsuario sesion) throws Exception {
        cargar("MenuView.fxml", "Gerencia - Taller Mecánico",
                clazz -> new MenuController(this, sesion));
    }

    public void showClientesView(SesionUsuario sesion) throws Exception {
        cargar("GestionClientesView.fxml", "Gestión de Clientes",
                clazz -> new GestionClientesController(new ClienteService(), this, sesion));
    }

    public void showVehiculosView(SesionUsuario sesion) throws Exception {
        cargar("VehiculoView.fxml", "Gestión de Vehículos",
                clazz -> new VehiculoController(new VehiculoService(), this, sesion));
    }

    public void showMecanicosView(SesionUsuario sesion) throws Exception {
        cargar("MecanicoView.fxml", "Gestión de Mecánicos",
                clazz -> new MecanicoController(new MecanicoService(), this, sesion));
    }

    public void showOrdenesView(SesionUsuario sesion) throws Exception {
        cargar("OrdenView.fxml", "Órdenes de Servicio",
                clazz -> new OrdenController(new OrdenService(), this, sesion));
    }

    public void showAsignarMecanicoView(SesionUsuario sesion) throws Exception {
        cargar("AsignarMecanicoView.fxml", "Asignar Mecánico",
                clazz -> new AsignarMecanicoController(new OrdenRepository(), new MecanicoRepository(), this, sesion));
    }

    public void showRepuestosView(SesionUsuario sesion) throws Exception {
        cargar("RepuestosView.fxml", "Inventario de Repuestos",
                clazz -> new RepuestoController(new RepuestoService(), this, sesion));
    }

    public void showPanelMecanicoView(SesionUsuario sesion) throws Exception {
        cargar("OrdenesMecanicoView.fxml", "Mis Órdenes - Mecánico",
                clazz -> new OrdenesMecanicoController(new OrdenService(), this, sesion));
    }

    public void showPanelClienteView(SesionUsuario sesion) throws Exception {
        cargar("ClienteView.fxml", "Portal del Cliente - Taller Mecánico",
                clazz -> new ClienteController(new VehiculoService(), new OrdenService(), this, sesion));
    }

    /**
     * Carga un FXML aplicándole un controllerFactory que sabe construir el
     * controller de esa pantalla con sus dependencias.
     */
    private void cargar(String fxml, String titulo, java.util.function.Function<Class<?>, Object> fabricaControlador) throws Exception {
        URL resource = SceneManager.class.getResource(FXML_PATH + fxml);
        if (resource == null) {
            throw new Exception("No se encontró el archivo FXML: " + FXML_PATH + fxml);
        }

        FXMLLoader loader = new FXMLLoader(resource);
        loader.setControllerFactory(clazz -> {
            try {
                return fabricaControlador.apply(clazz);
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el controller de " + fxml + ": " + e.getMessage(), e);
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root, ANCHO, ALTO);
        aplicarEstilos(scene);
        primaryStage.setTitle(titulo);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void aplicarEstilos(Scene scene) {
        URL css = SceneManager.class.getResource(CSS);
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
    }

  
    public void showInfoAlert(String titulo, String header, String contenido, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.initOwner(primaryStage);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        aplicarEstiloAlerta(alert);
        alert.showAndWait();
    }

 
    public boolean showConfirmAlert(String titulo, String header, String contenido) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(primaryStage);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        aplicarEstiloAlerta(alert);
        return alert.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }

    private void aplicarEstiloAlerta(Alert alert) {
        URL css = SceneManager.class.getResource(CSS);
        if (css != null) {
            alert.getDialogPane().getStylesheets().add(css.toExternalForm());
        }
    }
}
