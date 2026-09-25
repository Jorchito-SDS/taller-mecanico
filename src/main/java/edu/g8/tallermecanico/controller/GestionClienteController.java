package main.java.edu.g8.tallermecanico.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.edu.g8.tallermecanico.model.Cliente;
import main.java.edu.g8.tallermecanico.service.ClienteService;
import main.java.edu.g8.tallermecanico.util.SceneManager;
import main.java.edu.g8.tallermecanico.util.SesionUsuario;

/** CRUD de clientes para el perfil Gerente. */
public class GestionClientesController implements Initializable {

    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtBuscar;

    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;

    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, String> colDireccion;

    private final ClienteService clienteService;
    private final SceneManager sceneManager;
    private final SesionUsuario sesion;

    private ObservableList<Cliente> listaClientes;
    private Cliente seleccionActual;

    public GestionClientesController(ClienteService clienteService, SceneManager sceneManager, SesionUsuario sesion) {
        this.clienteService = clienteService;
        this.sceneManager = sceneManager;
        this.sesion = sesion;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, ant, actual) -> {
            seleccionActual = actual;
            if (actual != null) {
                cargarEnFormulario(actual);
            }
            actualizarEstadoBotones();
        });

        cargarTabla();
        actualizarEstadoBotones();
    }

    private void cargarTabla() {
        listaClientes = FXCollections.observableArrayList(clienteService.listarClientes());
        tablaClientes.setItems(listaClientes);
    }

    private void cargarEnFormulario(Cliente c) {
        txtNombre.setText(c.getNombre());
        txtTelefono.setText(c.getTelefono());
        txtEmail.setText(c.getEmail());
        txtDireccion.setText(c.getDireccion());
    }

    @FXML
    public void onGuardar() {
        Cliente cliente = leerFormulario();
        if (cliente == null) {
            return;
        }
        if (clienteService.registrarCliente(cliente)) {
            cargarTabla();
            limpiarCampos();
            sceneManager.showInfoAlert("Cliente registrado", null, "El cliente se guardó correctamente.", AlertType.INFORMATION);
        } else {
            sceneManager.showInfoAlert("No se pudo registrar", null, "Ocurrió un error al registrar el cliente.", AlertType.ERROR);
        }
    }

    @FXML
    public void onActualizar() {
        if (seleccionActual == null) {
            sceneManager.showInfoAlert("Selecciona un cliente", null, "Elige un cliente de la tabla para actualizarlo.", AlertType.WARNING);
            return;
        }
        Cliente cliente = leerFormulario();
        if (cliente == null) {
            return;
        }
        cliente.setIdCliente(seleccionActual.getIdCliente());
        if (clienteService.actualizarCliente(cliente)) {
            cargarTabla();
            limpiarCampos();
            sceneManager.showInfoAlert("Cliente actualizado", null, "Los cambios se guardaron correctamente.", AlertType.INFORMATION);
        } else {
            sceneManager.showInfoAlert("No se pudo actualizar", null, "Ocurrió un error al actualizar el cliente.", AlertType.ERROR);
        }
    }

    @FXML
    public void onEliminar() {
        if (seleccionActual == null) {
            sceneManager.showInfoAlert("Selecciona un cliente", null, "Elige un cliente de la tabla para eliminarlo.", AlertType.WARNING);
            return;
        }
        boolean confirmado = sceneManager.showConfirmAlert("Eliminar cliente", null,
                "¿Seguro que deseas eliminar a " + seleccionActual.getNombre() + "?");
        if (!confirmado) {
            return;
        }
        if (clienteService.eliminarCliente(seleccionActual.getIdCliente())) {
            cargarTabla();
            limpiarCampos();
        } else {
            sceneManager.showInfoAlert("No se pudo eliminar", null, "Ocurrió un error al eliminar el cliente.", AlertType.ERROR);
        }
    }

    @FXML
    public void onBuscar() {
        String filtro = txtBuscar.getText() == null ? "" : txtBuscar.getText().trim();
        if (filtro.isEmpty()) {
            cargarTabla();
            return;
        }
        listaClientes.setAll(clienteService.buscarClientes(filtro));
    }

    @FXML
    public void onLimpiar() {
        limpiarCampos();
    }

    @FXML
    public void onVolverMenu() throws Exception {
        sceneManager.showMenuGerenteView(sesion);
    }

    @FXML
    public void onCerrarSesion() throws Exception {
        sceneManager.showLoginView();
    }

    private Cliente leerFormulario() {
        String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
        String telefono = txtTelefono.getText() == null ? "" : txtTelefono.getText().trim();
        String email = txtEmail.getText() == null ? "" : txtEmail.getText().trim();
        String direccion = txtDireccion.getText() == null ? "" : txtDireccion.getText().trim();

        if (nombre.isEmpty() || email.isEmpty()) {
            sceneManager.showInfoAlert("Campos incompletos", null, "El nombre y el correo son obligatorios.", AlertType.WARNING);
            return null;
        }
        return new Cliente(0, nombre, telefono, email, direccion);
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtDireccion.clear();
        seleccionActual = null;
        tablaClientes.getSelectionModel().clearSelection();
        actualizarEstadoBotones();
    }

    private void actualizarEstadoBotones() {
        boolean haySeleccion = seleccionActual != null;
        if (btnActualizar != null) btnActualizar.setDisable(!haySeleccion);
        if (btnEliminar != null) btnEliminar.setDisable(!haySeleccion);
    }
}
