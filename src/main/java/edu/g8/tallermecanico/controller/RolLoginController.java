package main.java.edu.g8.tallermecanico.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
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

public class RolLoginController implements Initializable {

    @FXML private VBox contenedor;
    @FXML private Label lblIcono;
    @FXML private Label lblTitulo;
    @FXML private Label lblEtiquetaUsuario;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;
    @FXML private Hyperlink linkRegistro;

    @FXML private VBox panelSeleccionRol;
    @FXML private VBox panelCliente;
    @FXML private VBox panelMecanico;
    @FXML private VBox panelGerencia;

    @FXML private TextField txtEmailCliente;
    @FXML private PasswordField txtPasswordCliente;

    @FXML private TextField txtNombreMecanico;
    @FXML private PasswordField txtPasswordMecanico;

    @FXML private TextField txtUsuarioGerencia;
    @FXML private PasswordField txtPasswordGerencia;

    // null = todavía no se ha elegido perfil -> se muestra el panel de selección.
    private Rol rol = null;
    private ClienteService clienteService;
    private MecanicoService mecanicoService;
    private SceneManager sceneManager;

    public RolLoginController() {
        this.clienteService = new ClienteService();
        this.mecanicoService = new MecanicoService();
    }

    public RolLoginController(Rol rol, ClienteService clienteService, MecanicoService mecanicoService, SceneManager sceneManager) {
        this.rol = rol;
        this.clienteService = clienteService != null ? clienteService : new ClienteService();
        this.mecanicoService = mecanicoService != null ? mecanicoService : new MecanicoService();
        this.sceneManager = sceneManager;
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
        actualizarInterfazRol();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        actualizarInterfazRol();
    }

    private void actualizarInterfazRol() {
        if (rol == null) return;

        if (lblIcono != null && rol.getIcono() != null) lblIcono.setText(rol.getIcono());
        if (lblTitulo != null && rol.getTituloLogin() != null) lblTitulo.setText(rol.getTituloLogin());
        if (lblEtiquetaUsuario != null && rol.getEtiquetaUsuario() != null) lblEtiquetaUsuario.setText(rol.getEtiquetaUsuario());
        if (txtUsuario != null && rol.getPromptUsuario() != null) txtUsuario.setPromptText(rol.getPromptUsuario());

        if (contenedor != null && rol.getCssClass() != null) {
            contenedor.getStyleClass().removeAll("role-cliente", "role-mecanico", "role-gerente");
            contenedor.getStyleClass().add(rol.getCssClass());
        }

        mostrarPanelCorrespondiente();
    }

    private void mostrarPanelCorrespondiente() {
        if (panelSeleccionRol == null) return;
        panelSeleccionRol.setVisible(false);
        if (panelCliente != null) panelCliente.setVisible(rol == Rol.CLIENTE);
        if (panelMecanico != null) panelMecanico.setVisible(rol == Rol.MECANICO);
        if (panelGerencia != null) panelGerencia.setVisible(rol == Rol.GERENTE);
    }

    private void mostrarPanelSeleccion() {
        if (panelSeleccionRol != null) panelSeleccionRol.setVisible(true);
        if (panelCliente != null) panelCliente.setVisible(false);
        if (panelMecanico != null) panelMecanico.setVisible(false);
        if (panelGerencia != null) panelGerencia.setVisible(false);
    }

    // --- SELECCIÓN DE ROL ---

    @FXML
    public void onSeleccionarCliente(ActionEvent event) {
        setRol(Rol.CLIENTE);
    }

    @FXML
    public void onSeleccionarMecanico(ActionEvent event) {
        setRol(Rol.MECANICO);
    }

    @FXML
    public void onSeleccionarGerencia(ActionEvent event) {
        setRol(Rol.GERENTE);
    }

    @FXML
    public void onSeleccionarGerente(ActionEvent event) {
        setRol(Rol.GERENTE);
    }

    // --- MANEJADORES DE INGRESO (LOGIN) ---

    @FXML
    public void onIngresarCliente(ActionEvent event) throws Exception {
        setRol(Rol.CLIENTE);
        String email = obtenerTexto(txtEmailCliente, txtUsuario);
        String pass = obtenerTexto(txtPasswordCliente, txtPassword);
        ingresarComoCliente(email, pass);
    }

    @FXML
    public void onIngresarMecanico(ActionEvent event) throws Exception {
        setRol(Rol.MECANICO);
        String nombre = obtenerTexto(txtNombreMecanico, txtUsuario);
        String pass = obtenerTexto(txtPasswordMecanico, txtPassword);
        ingresarComoMecanico(nombre, pass);
    }

    @FXML
    public void onIngresarGerente(ActionEvent event) throws Exception {
        onIngresarGerencia(event);
    }

    @FXML
    public void onIngresarGerencia(ActionEvent event) throws Exception {
        setRol(Rol.GERENTE);
        String usr = obtenerTexto(txtUsuarioGerencia, txtUsuario);
        String pass = obtenerTexto(txtPasswordGerencia, txtPassword);
        ingresarComoGerente(usr, pass);
    }

    @FXML
    public void onIngresar(ActionEvent event) throws Exception {
        switch (rol) {
            case CLIENTE -> onIngresarCliente(event);
            case MECANICO -> onIngresarMecanico(event);
            case GERENTE -> onIngresarGerencia(event);
        }
    }

    private String obtenerTexto(TextField principal, TextField secundario) {
        if (principal != null && principal.getText() != null && !principal.getText().trim().isEmpty()) {
            return principal.getText().trim();
        }
        return (secundario != null && secundario.getText() != null) ? secundario.getText().trim() : "";
    }

    private void mostrarMensaje(String mensaje) {
        if (lblMensaje != null) {
            lblMensaje.setText(mensaje);
        }
    }

    private void ingresarComoCliente(String email, String password) throws Exception {
        if (email.isEmpty()) {
            mostrarMensaje("Ingresa tu correo electrónico.");
            return;
        }
        if (password.isEmpty()) {
            mostrarMensaje("Ingresa tu contraseña.");
            return;
        }

        Cliente cliente = clienteService.autenticar(email, password);

        if (cliente == null) {
            mostrarMensaje("Correo o contraseña incorrectos.");
            return;
        }

        SesionUsuario sesion = new SesionUsuario(Rol.CLIENTE, cliente.getNombre(), String.valueOf(cliente.getIdCliente()));
        if (sceneManager != null) {
            sceneManager.showPanelClienteView(sesion);
        }
    }

    private void ingresarComoMecanico(String nombre, String password) throws Exception {
        if (nombre.isEmpty()) {
            mostrarMensaje("Ingresa tu nombre.");
            return;
        }
        if (password.isEmpty()) {
            mostrarMensaje("Ingresa tu contraseña.");
            return;
        }
        Mecanico mecanico = mecanicoService.autenticar(nombre, password);
        if (mecanico == null) {
            mostrarMensaje("Nombre o contraseña incorrectos.");
            return;
        }
        SesionUsuario sesion = new SesionUsuario(Rol.MECANICO, mecanico.getNombre(), mecanico.getIdMecanico());
        if (sceneManager != null) {
            sceneManager.showPanelMecanicoView(sesion);
        }
    }

    private void ingresarComoGerente(String usuario, String password) throws Exception {
        if (usuario.isEmpty()) {
            mostrarMensaje("Ingresa tu usuario.");
            return;
        }
        if (password.isEmpty()) {
            mostrarMensaje("Ingresa tu contraseña.");
            return;
        }
        if (!AdminCredentials.autenticar(usuario, password)) {
            mostrarMensaje("Usuario o contraseña incorrectos.");
            return;
        }
        SesionUsuario sesion = new SesionUsuario(Rol.GERENTE, "Gerencia", null);
        if (sceneManager != null) {
            sceneManager.showMenuGerenteView(sesion);
        }
    }

    // --- NAVEGACIÓN Y REGISTRO ---

    @FXML
    public void onIrARegistroCliente(ActionEvent event) throws Exception {
        onIrARegistro(event);
    }

    @FXML
    public void onIrARegistroMecanico(ActionEvent event) throws Exception {
        if (sceneManager != null) {
            sceneManager.showRegistroMecanicoView();
        }
    }

    @FXML
    public void onIrARegistro(ActionEvent event) throws Exception {
        if (sceneManager != null) {
            sceneManager.showRegistroClienteView();
        }
    }

    @FXML
    public void onVolverPerfil(ActionEvent event) throws Exception {
        if (panelSeleccionRol != null) {
            mostrarPanelSeleccion();
        } else {
            onVolver(event);
        }
    }

    @FXML
    public void onVolverLogin(ActionEvent event) throws Exception {
        onVolver(event);
    }

    @FXML
    public void onVolver(ActionEvent event) throws Exception {
        if (sceneManager != null) {
            sceneManager.showLoginView();
        }
    }
}