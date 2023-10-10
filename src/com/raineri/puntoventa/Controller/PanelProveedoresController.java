/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.raineri.puntoventa.Controller;

import com.raineri.puntoventa.Entity.Proveedor;
import com.raineri.puntoventa.Jpa.ProveedorJpaController;
import com.raineri.puntoventa.Jpa.exceptions.NonexistentEntityException;
import com.raineri.puntoventa.util.Alerta;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author exera
 */
public class PanelProveedoresController implements Initializable {

    @FXML
    private TableView<Proveedor> tablaProveedor;
    @FXML
    private TableColumn<Proveedor, Integer> col_id;
    @FXML
    private TableColumn<Proveedor, String> col_nombre;
    @FXML
    private TableColumn<Proveedor, String> col_cuit;
    @FXML
    private TableColumn<Proveedor, String> col_telefono;
    @FXML
    private TableColumn<Proveedor, String> col_fechaRegistro;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnAgregarProveedor;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCuit;
    @FXML
    private TextField txtCorreo;

    private Proveedor proveedorModificar;
    ObservableList<Proveedor> proveedoresObs;
    ProveedorJpaController proveedorDao;
    SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yy\tHH:mm:ss");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_cuit.setCellValueFactory(new PropertyValueFactory<>("cuit"));
        col_telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        col_fechaRegistro.setCellValueFactory((param) -> {
            return new SimpleObjectProperty<>(sf.format(param.getValue().getFechaRegistro()));
        });

        listarProveedores();
    }

    @FXML
    private void EliminarProveedor(ActionEvent event) {
        if (tablaProveedor.getSelectionModel().getSelectedItem() != null) {

            int idProveedor = tablaProveedor.getSelectionModel().getSelectedItem().getId();
            Proveedor proveedor = proveedorDao.findProveedor(idProveedor);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Importante");
            alert.setContentText("Seguro desea eliminar el proveedor:\nNombre: " + proveedor.getNombre() + "\tCuit: " + proveedor.getCuit());
            alert.setHeaderText("Eliminar Proveedor");
            ButtonType btnsi = new ButtonType("Si");
            ButtonType btnno = new ButtonType("No");
            alert.getButtonTypes().setAll(btnsi, btnno);

            Optional<ButtonType> showAndWait = alert.showAndWait();
            if (showAndWait.get() == btnsi) {

                try {
                    proveedorDao.destroy(idProveedor);
                } catch (NonexistentEntityException ex) {
                    java.util.logging.Logger.getLogger(PanelProveedoresController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }

            }
            listarProveedores();
            alert.close();
        } else {
            Alerta.mostrarAlertaAdvertencia("Debe seleccionar un proveedor");
        }
    }

    @FXML
    private void FiltrarBusqueda(KeyEvent event) {
        proveedorDao = new ProveedorJpaController();
        proveedoresObs = FXCollections.observableArrayList();
        List<Proveedor> lista_proveedor = proveedorDao.findProveedor(txtBuscar.getText());
        for (Proveedor p : lista_proveedor) {
            proveedoresObs.add(p);
        }

        tablaProveedor.setItems(proveedoresObs);
    }

    @FXML
    private void nuevoProveedor(ActionEvent event) {
        if (txtNombre.getText().isEmpty() || txtCuit.getText().isEmpty() || txtTelefono.getText().isEmpty()) {
            Alerta.mostrarAlertaAdvertencia("Existen campos vacios");
        } else {
            proveedorDao = new ProveedorJpaController();
            Proveedor proveedor = new Proveedor();
            proveedor.setNombre(txtNombre.getText());
            proveedor.setTelefono(txtTelefono.getText());
            proveedor.setCuit(txtCuit.getText());
            proveedor.setCorreo(txtCorreo.getText());
            proveedor.setFechaRegistro(new Date());
            proveedorDao.create(proveedor);
            Alerta.mostrarAlertaInformacion("Proveedor cargado exitosamente");
            listarProveedores();
            limpiarCampos();
        }
    }

    private void listarProveedores() {
        tablaProveedor.getItems().clear();
        proveedoresObs = FXCollections.observableArrayList();
        proveedorDao = new ProveedorJpaController();
        List<Proveedor> proveedores = proveedorDao.findProveedorEntities();
        for (Proveedor p : proveedores) {
            proveedoresObs.add(p);
        }
        tablaProveedor.setItems(proveedoresObs);
    }

    private void limpiarCampos() {
        txtCorreo.setText(null);
        txtCuit.setText(null);
        txtNombre.setText(null);
        txtTelefono.setText(null);
    }

    @FXML
    private void EditarProveedor(ActionEvent event) {
        if (tablaProveedor.getSelectionModel().getSelectedItem() != null) {
            int idProveedor = tablaProveedor.getSelectionModel().getSelectedItem().getId();
            proveedorModificar = proveedorDao.findProveedor(idProveedor);

            txtCorreo.setText(proveedorModificar.getCorreo());
            txtCuit.setText(proveedorModificar.getCuit());
            txtNombre.setText(proveedorModificar.getNombre());
            txtTelefono.setText(proveedorModificar.getTelefono());

            btnAgregarProveedor.setText("Actualizar");
            btnAgregarProveedor.setOnAction((param) -> {
                try {
                    proveedorModificar.setTelefono(txtTelefono.getText());
                    proveedorModificar.setCorreo(txtCorreo.getText());
                    proveedorModificar.setNombre(txtNombre.getText());
                    proveedorModificar.setCuit(txtCuit.getText());

                    proveedorDao.edit(proveedorModificar);
                    Alerta.mostrarAlertaInformacion("Proveedor acualizado exitosamente!");
                    proveedorModificar = null;
                    limpiarCampos();
                    listarProveedores();
                } catch (Exception ex) {
                    Alerta.mostrarAlertaAdvertencia("Hubo un error: " + ex.getMessage());
                    java.util.logging.Logger.getLogger(PanelProveedoresController.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            });

        } else {
            Alerta.mostrarAlertaAdvertencia("Debe seleccionar un proveedor!");

        }
    }

}
