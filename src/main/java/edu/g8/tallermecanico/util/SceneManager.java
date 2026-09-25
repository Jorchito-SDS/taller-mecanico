package main.java.edu.g8.tallermecanico.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;

import main.java.edu.g8.tallermecanico.controller.AsignarMecanicoController;
import main.java.edu.g8.tallermecanico.controller.ClienteController;
import main.java.edu.g8.tallermecanico.controller.GestionClientesController;
import main.java.edu.g8.tallermecanico.controller.MecanicoController;
import main.java.edu.g8.tallermecanico.controller.MenuController;
import main.java.edu.g8.tallermecanico.controller.OrdenController;
import main.java.edu.g8.tallermecanico.controller.OrdenesMecanicoController;
import main.java.edu.g8.tallermecanico.controller.RepuestoController;
import main.java.edu.g8.tallermecanico.repository.MecanicoRepository;
import main.java.edu.g8.tallermecanico.repository.OrdenRepository;
import main.java.edu.g8.tallermecanico.service.ClienteService;
import main.java.edu.g8.tallermecanico.service.MecanicoService;
import main.java.edu.g8.tallermecanico.service.OrdenService;
import main.java.edu.g8.tallermecanico.service.RepuestoService;
import main.java.edu.g8.tallermecanico.service.VehiculoService;

public class SceneManager {

    private final Stage primaryStage;
    private SesionUsuario sesion;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.sesion = new SesionUsuario(null, null, null);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public SesionUsuario getSesion() {
        return sesion;
    }

    public void setSesion(SesionUsuario sesion) {
        this.sesion = sesion;
    }

    // --- MÉTODOS DE VISTAS Y NAVEGACIÓN ---

    public void showLoginView() {
        cargarVista("/main/java/edu/g8/tallermecanico/view/RolLoginView.fxml", "Iniciar Sesión - Taller Mecánico");
    }
public void showRegistroMecanicoView() {
        cargarVista("/main/java/edu/g8/tallermecanico/view/RegistroMecanicoView.fxml", "Registro de Mecánico - Taller Mecánico");
    }
    public void mostrarLogin() {
        showLoginView();
    }

    public void showRolLoginView(Rol rol) {
        showLoginView();
    }

    public void showRegistroClienteView() {
        cargarVista("/main/java/edu/g8/tallermecanico/view/RegistroClienteView.fxml", "Registro de Cliente - Taller Mecánico");
    }

    public void showMenuGerenteView(SesionUsuario sesion) {
        this.sesion = sesion;
        cargarVista("/main/java/edu/g8/tallermecanico/view/MenuView.fxml", "Menú Principal - Gerencia");
    }

    public void showClientesView(SesionUsuario sesion) {
        this.sesion = sesion;
        cargarVista("/main/java/edu/g8/tallermecanico/view/GestionClientesView.fxml", "Gestión de Clientes");
    }

    public void showVehiculosView(SesionUsuario sesion) {
        this.sesion = sesion;
        cargarVista("/main/java/edu/g8/tallermecanico/view/VehiculoView.fxml", "Gestión de Vehículos");
    }

    public void showMecanicosView(SesionUsuario sesion) {
        this.sesion = sesion;
        cargarVista("/main/java/edu/g8/tallermecanico/view/MecanicoView.fxml", "Gestión de Mecánicos");
    }

    public void showOrdenesView(SesionUsuario sesion) {
        this.sesion = sesion;
        cargarVista("/main/java/edu/g8/tallermecanico/view/OrdenView.fxml", "Gestión de Órdenes de Servicio");
    }

    public void showAsignarMecanicoView(SesionUsuario sesion) {
        this.sesion = sesion;
        cargarVista("/main/java/edu/g8/tallermecanico/view/AsignarMecanicoView.fxml", "Asignar Mecánico");
    }

    public void showRepuestosView(SesionUsuario sesion) {
        this.sesion = sesion;
        cargarVista("/main/java/edu/g8/tallermecanico/view/RepuestosView.fxml", "Gestión de Repuestos");
    }

    public void showPanelClienteView(SesionUsuario sesion) {
        this.sesion = sesion;
        cargarVista("/main/java/edu/g8/tallermecanico/view/ClienteView.fxml", "Portal de Clientes");
    }

    public void showPanelMecanicoView(SesionUsuario sesion) {
        this.sesion = sesion;
        cargarVista("/main/java/edu/g8/tallermecanico/view/OrdenesMecanicoView.fxml", "Portal de Mecánicos");
    }

    // --- CARGADOR ROBUSTO DE VISTAS FXML ---

    private void cargarVista(String fxmlPath, String titulo) {
        String nombreArchivo = fxmlPath.substring(fxmlPath.lastIndexOf("/") + 1);

        // Intenta localizar la vista FXML dinámicamente según la compilación de NetBeans
        String[] rutasCandidatas = {
            fxmlPath,
            "/main/java/edu/g8/tallermecanico/view/" + nombreArchivo,
            "/edu/g8/tallermecanico/view/" + nombreArchivo,
            "/view/" + nombreArchivo,
            "/resources/view/" + nombreArchivo,
            "/" + nombreArchivo
        };

        URL resourceUrl = null;
        for (String ruta : rutasCandidatas) {
            resourceUrl = getClass().getResource(ruta);
            if (resourceUrl != null) {
                break;
            }
        }

        if (resourceUrl == null) {
            showInfoAlert("Error de Carga", "No se encontró el archivo FXML",
                    "No se pudo hallar '" + nombreArchivo + "' en las rutas de compilación.\nComprueba que el archivo exista dentro del proyecto.",
                    AlertType.ERROR);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(resourceUrl);
            loader.setControllerFactory(this::crearControlador);
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller != null) {
                try {
                    controller.getClass().getMethod("setSceneManager", SceneManager.class).invoke(controller, this);
                } catch (NoSuchMethodException ignored) {
                }
            }

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(titulo);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showInfoAlert("Error de Carga", "No se pudo procesar la vista",
                    "Detalle del error:\n" + e.getMessage(), AlertType.ERROR);
        }
    }

    // --- FÁBRICA DE CONTROLADORES ---
    // Varios controladores (Menu, Cliente, GestionClientes, Mecanico, Orden,
    // OrdenesMecanico, Repuesto, AsignarMecanico) solo tienen constructor con
    // parámetros (servicios + SceneManager + sesión) y NO tienen constructor
    // vacío. FXMLLoader, por defecto, solo sabe crear controladores con
    // constructor vacío, así que sin esta fábrica el load() de esas vistas
    // lanza una excepción (se veía como "no se pudo procesar la vista" o la
    // pantalla simplemente no abría / no guardaba nada).
    private Object crearControlador(Class<?> claseControlador) {
        try {
            if (claseControlador == MenuController.class) {
                return new MenuController(this, sesion);
            }
            if (claseControlador == GestionClientesController.class) {
                return new GestionClientesController(new ClienteService(), this, sesion);
            }
            if (claseControlador == ClienteController.class) {
                return new ClienteController(new VehiculoService(), new OrdenService(), this, sesion);
            }
            if (claseControlador == MecanicoController.class) {
                return new MecanicoController(new MecanicoService(), this, sesion);
            }
            if (claseControlador == OrdenController.class) {
                return new OrdenController(new OrdenService(), this, sesion);
            }
            if (claseControlador == OrdenesMecanicoController.class) {
                return new OrdenesMecanicoController(new OrdenService(), this, sesion);
            }
            if (claseControlador == RepuestoController.class) {
                return new RepuestoController(new RepuestoService(), this, sesion);
            }
            if (claseControlador == AsignarMecanicoController.class) {
                return new AsignarMecanicoController(new OrdenRepository(), new MecanicoRepository(), this, sesion);
            }
            // Resto de controladores (RolLoginController, RegistroClienteController,
            // RegistroMecanicoController, VehiculoController, ...) sí tienen
            // constructor vacío: se instancian de la forma estándar.
            return claseControlador.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear el controlador " + claseControlador.getName(), e);
        }
    }

    // --- ALERTAS REUTILIZABLES ---

    public void showInfoAlert(String title, String header, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public boolean showConfirmAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}