package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;
import main.java.edu.g8.tallermecanico.config.AdminCredentials;
import main.java.edu.g8.tallermecanico.model.Cliente;
import main.java.edu.g8.tallermecanico.model.Mecanico;
import main.java.edu.g8.tallermecanico.service.ClienteService;
import main.java.edu.g8.tallermecanico.service.MecanicoService;
import main.java.edu.g8.tallermecanico.util.Rol;
import main.java.edu.g8.tallermecanico.util.SceneManager;
import main.java.edu.g8.tallermecanico.util.SesionUsuario;

/**
 * Pantalla de inicio de sesión compartida por los tres perfiles. El Rol con
 * el que se abrió llega por constructor (lo decide LoginController) y define
 * el título/color/campo de esta misma vista; cada perfil valida contra su
 * propia fuente de datos.
 */
public class RolLoginController implements Initializable {

    @FXML private VBox contenedor;
    @FXML private Label lblIcono;
    @FXML private Label lblTitulo;
    @FXML private Label lblEtiquetaUsuario;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    private final Rol rol;
    private final ClienteService clienteService;
    private final MecanicoService mecanicoService;
    private final SceneManager sceneManager;

    public RolLoginController(Rol rol, ClienteService clienteService, MecanicoService mecanicoService, SceneManager sceneManager) {
        this.rol = rol;
        this.clienteService = clienteService;
        this.mecanicoService = mecanicoService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblIcono.setText(rol.getIcono());
        lblTitulo.setText(rol.getTituloLogin());
        lblEtiquetaUsuario.setText(rol.getEtiquetaUsuario());
        txtUsuario.setPromptText(rol.getPromptUsuario());

        contenedor.getStyleClass().removeAll("role-cliente", "role-mecanico", "role-gerente");
        contenedor.getStyleClass().add(rol.getCssClass());

        // El portal de Cliente identifica por correo; no maneja contraseña propia.
        boolean requierePassword = rol != Rol.CLIENTE;
        txtPassword.setVisible(requierePassword);
        txtPassword.setManaged(requierePassword);
    }

    @FXML
    public void onIngresar(ActionEvent event) throws Exception {
        String usuario = txtUsuario.getText() == null ? "" : txtUsuario.getText().trim();
        String password = txtPassword.getText() == null ? "" : txtPassword.getText();

        if (usuario.isEmpty()) {
            lblMensaje.setText("Completa el campo de usuario.");
            return;
        }

        switch (rol) {
            case CLIENTE -> ingresarComoCliente(usuario);
            case MECANICO -> ingresarComoMecanico(usuario, password);
            case GERENTE -> ingresarComoGerente(usuario, password);
        }
    }

    private void ingresarComoCliente(String email) throws Exception {
        Cliente cliente = clienteService.autenticarPorEmail(email);
        if (cliente == null) {
            lblMensaje.setText("No encontramos un cliente con ese correo.");
            return;
        }
        SesionUsuario sesion = new SesionUsuario(Rol.CLIENTE, cliente.getNombre(), String.valueOf(cliente.getIdCliente()));
        sceneManager.showPanelClienteView(sesion);
    }

    private void ingresarComoMecanico(String nombre, String password) throws Exception {
        if (password.isEmpty()) {
            lblMensaje.setText("Ingresa tu contraseña.");
            return;
        }
        Mecanico mecanico = mecanicoService.autenticar(nombre, password);
        if (mecanico == null) {
            lblMensaje.setText("Nombre o contraseña incorrectos.");
            return;
        }
        SesionUsuario sesion = new SesionUsuario(Rol.MECANICO, mecanico.getNombre(), mecanico.getIdMecanico());
        sceneManager.showPanelMecanicoView(sesion);
    }

    private void ingresarComoGerente(String usuario, String password) throws Exception {
        if (!AdminCredentials.autenticar(usuario, password)) {
            lblMensaje.setText("Usuario o contraseña incorrectos.");
            return;
        }
        SesionUsuario sesion = new SesionUsuario(Rol.GERENTE, "Gerencia", null);
        sceneManager.showMenuGerenteView(sesion);
    }

    @FXML
    public void onVolver(ActionEvent event) throws Exception {
        sceneManager.showLoginView();
    }
}
