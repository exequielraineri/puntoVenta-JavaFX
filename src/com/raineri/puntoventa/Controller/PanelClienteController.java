/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.raineri.puntoventa.Controller;

import com.raineri.puntoventa.Entity.Cliente;
import com.raineri.puntoventa.Jpa.ClienteJpaController;
import com.raineri.puntoventa.Jpa.exceptions.NonexistentEntityException;
import com.raineri.puntoventa.util.Alerta;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author exera
 */
public class PanelClienteController implements Initializable {

    @FXML
    private Button btnAgregarCliente;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtCuit;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDomicilio;
    @FXML
    private TableColumn<Cliente, Integer> col_id;
    @FXML
    private TableColumn<Cliente, String> col_nombre;
    @FXML
    private TableColumn<Cliente, String> col_apellido;
    @FXML
    private TableColumn<Cliente, String> col_cuit;
    @FXML
    private TableColumn<Cliente, String> col_telefono;
    @FXML
    private TableColumn<Cliente, String> col_domicilio;
    @FXML
    private TableColumn<Cliente, String> col_fechaRegistro;
    @FXML
    private TextField txtBuscar;

    private ClienteJpaController clienteDao;
    @FXML
    private TableView<Cliente> tablaClientes;

    private Cliente cliente;
    @FXML
    private Accordion accordion;
    @FXML
    private TitledPane accordionNuevoCliente;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        clienteDao = new ClienteJpaController();
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_apellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        col_cuit.setCellValueFactory(new PropertyValueFactory<>("cuit"));
        col_domicilio.setCellValueFactory(new PropertyValueFactory<>("domicilio"));
        col_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        col_fechaRegistro.setCellValueFactory((param) -> {
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yy");
            return new SimpleObjectProperty<>(sf.format(param.getValue().getFechaRegistro()));
        });

        accordion.setExpandedPane(accordionNuevoCliente);
        listarTabla();
        formatearEntradas();
    }

    @FXML
    private void nuevoCliente(ActionEvent event) {
        if (validarEntradas()) {
            cliente = new Cliente();
            cliente.setNombre(txtNombre.getText());
            cliente.setApellido(txtApellido.getText());
            cliente.setCuit(txtCuit.getText());
            cliente.setDomicilio(txtDomicilio.getText());
            cliente.setTelefono(txtTelefono.getText());
            cliente.setFechaRegistro(new Date());

            clienteDao.create(cliente);
            Alerta.mostrarAlertaInformacion("Cliente creado exitosamente.");
            listarTabla();
            limpiarCampos();
        } else {
            Alerta.mostrarAlertaAdvertencia("Existen campos vacios");
        }
    }

    @FXML
    private void EliminarCliente(ActionEvent event) {
        if (tablaClientes.getSelectionModel().getSelectedItem() != null) {
            Cliente clienteAEliminar = tablaClientes.getSelectionModel().getSelectedItem();
            if (Alerta.mostrarAlertaConfirmation(String.format("Seguro desea eliminar el cliente %s", clienteAEliminar.getApellido().concat(",").concat(clienteAEliminar.getNombre())))) {
                try {
                    clienteDao.destroy(clienteAEliminar.getId());
                    Alerta.mostrarAlertaInformacion("Cliente eliminado exitosamente.");
                    listarTabla();
                    limpiarCampos();
                } catch (NonexistentEntityException ex) {
                    Alerta.mostrarAlertaError("Hubo un error: " + ex.getMessage() + "\nLocalizado: " + ex.getLocalizedMessage());
                    Logger.getLogger(PanelClienteController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            Alerta.mostrarAlertaAdvertencia("Debe seleccionar un cliente.");
        }
    }

    @FXML
    private void EditarCliente(ActionEvent event) {
        if (tablaClientes.getSelectionModel().getSelectedItem() != null) {
            cliente = tablaClientes.getSelectionModel().getSelectedItem();
            txtApellido.setText(cliente.getApellido());
            txtCuit.setText(cliente.getCuit());
            txtNombre.setText(cliente.getNombre());
            txtDomicilio.setText(cliente.getDomicilio());
            txtTelefono.setText(cliente.getTelefono());

            btnAgregarCliente.setText("Actualizar");
            btnAgregarCliente.setOnAction((param) -> {
                if (validarEntradas()) {
                    try {
                        cliente.setApellido(txtApellido.getText());
                        cliente.setCuit(txtCuit.getText());
                        cliente.setDomicilio(txtDomicilio.getText());
                        cliente.setNombre(txtNombre.getText());
                        cliente.setTelefono(txtTelefono.getText());
                        clienteDao.edit(cliente);
                        Alerta.mostrarAlertaInformacion("Cliente actualizado exitosamente.");
                        listarTabla();
                        limpiarCampos();
                    } catch (Exception ex) {
                        Alerta.mostrarAlertaError("Hubo un error: " + ex.getMessage() + "\nLocalizado: " + ex.getLocalizedMessage());
                        Logger.getLogger(PanelClienteController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    Alerta.mostrarAlertaAdvertencia("Existen campos vacios");
                }
            });
        } else {
            Alerta.mostrarAlertaAdvertencia("Debe seleccionar un cliente.");
        }
    }

    @FXML
    private void FiltrarBusqueda(KeyEvent event) {
        tablaClientes.getItems().clear();
        ObservableList<Cliente> clienteObs = FXCollections.observableArrayList();
        List<Cliente> clientes = clienteDao.findClienteEntitiesFiltro(txtBuscar.getText());
        for (Cliente c : clientes) {
            clienteObs.add(c);
        }

        tablaClientes.setItems(clienteObs);

    }

    private void listarTabla() {
        tablaClientes.getItems().clear();
        ObservableList<Cliente> clienteObs = FXCollections.observableArrayList();
        List<Cliente> clientes = clienteDao.findClienteEntities();
        for (Cliente c : clientes) {
            clienteObs.add(c);
        }
        tablaClientes.setItems(clienteObs);

    }

    private boolean validarEntradas() {
        boolean valido = true;
        if (txtNombre.getText() == null) {
            valido = false;
            txtNombre.setStyle("-fx-border-color:red");
        } else {
            txtNombre.setStyle("-fx-border-color:#84b6f4");

        }

        if (txtApellido.getText() == null) {
            valido = false;
            txtApellido.setStyle("-fx-border-color:red");
        } else {
            txtApellido.setStyle("-fx-border-color:#84b6f4");

        }

        if (txtCuit.getText() == null) {
            valido = false;
            txtCuit.setStyle("-fx-border-color:red");
        } else {
            txtCuit.setStyle("-fx-border-color:#84b6f4");

        }

        if (txtDomicilio.getText() == null) {
            valido = false;
            txtDomicilio.setStyle("-fx-border-color:red");
        } else {
            txtDomicilio.setStyle("-fx-border-color:#84b6f4");

        }

        if (txtTelefono.getText() == null) {
            valido = false;
            txtTelefono.setStyle("-fx-border-color:red");
        } else {
            txtTelefono.setStyle("-fx-border-color:#84b6f4");

        }

        return valido;
    }

    private void limpiarCampos() {
        txtApellido.setText(null);
        txtCuit.setText(null);
        txtNombre.setText(null);
        txtDomicilio.setText(null);
        txtTelefono.setText(null);
    }

    private void formatearEntradas() {
        txtTelefono.setTextFormatter(new TextFormatter<>(
                (change) -> {
                    String text = change.getControlNewText();
                    for (int i = 0; i < text.length(); i++) {
                        if (!text.matches("^\\d+$")) {
                            txtTelefono.setStyle("-fx-border-color:red");
                            return null;
                        }
                        txtTelefono.setStyle("-fx-border-color:#84b6f4");
                    }
                    return change;

                }
        ));

        txtCuit.setTextFormatter(new TextFormatter<>(
                (change) -> {
                    String text = change.getControlNewText();
                    for (int i = 0; i < text.length(); i++) {
                        if (text.length() > 11) {
                            return null;
                        } else {
                            if (text.matches("^\\d+$")) {
                                txtCuit.setStyle("-fx-border-color:#84b6f4");
                                return change;
                            } else {
                                txtCuit.setStyle("-fx-border-color:red");
                                return change;
                            }
                        }
                    }
                    return change;
                }
        ));

    }

}
