/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.raineri.puntoventa.Controller;

import static com.raineri.puntoventa.Controller.PanelInventarioController.productoModificar;
import com.raineri.puntoventa.Entity.Producto;
import com.raineri.puntoventa.Entity.Proveedor;
import com.raineri.puntoventa.Jpa.ProductoJpaController;
import com.raineri.puntoventa.Jpa.ProveedorJpaController;
import com.raineri.puntoventa.util.Alerta;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author exera
 */
public class PanelAgregarProductoController implements Initializable {

    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtStock;
    @FXML
    private Button btnAgregar;
    @FXML
    private ComboBox<String> txtCategoria;

    private ProductoJpaController productoDao = new ProductoJpaController();

    private Producto producto;
    @FXML
    private Label lblTitulo;

    @FXML
    private ComboBox<Proveedor> comboProveedor;

    
    private ProveedorJpaController proveedorDao;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        ObservableList<String> categorias = FXCollections.observableArrayList();
        categorias.add("Ruleman");
        categorias.add("Reten");
        categorias.add("Accesorios");
        categorias.add("Caja");
        
        cargarComboProveedores();
        txtCategoria.setItems(categorias);
        if (productoModificar != null) {
            txtCodigo.setText(productoModificar.getCodigo());
            txtDescripcion.setText(productoModificar.getDescripcion());
            txtPrecio.setText(productoModificar.getPrecio().toString());
            txtStock.setText(productoModificar.getStock().toString());
            txtCategoria.getSelectionModel().select(productoModificar.getCategoria());
            comboProveedor.getSelectionModel().select(productoModificar.getProveedor());
        }
        formatearEntradas();
    }

    @FXML
    private void guardarProducto(ActionEvent event) {
        productoDao=new ProductoJpaController();
        if (productoModificar != null) {
            try {
                productoModificar.setDescripcion(txtDescripcion.getText());
                productoModificar.setCodigo(txtCodigo.getText());
                productoModificar.setCategoria(txtCategoria.getValue());
                productoModificar.setStock(Integer.valueOf(txtStock.getText()));
                productoModificar.setPrecio(Double.valueOf(txtPrecio.getText()));
                productoModificar.setProveedor(comboProveedor.getSelectionModel().getSelectedItem());
                productoDao.edit(productoModificar);
                Alerta.mostrarAlertaInformacion("Se modifico el producto exitosamente!");
                limpiarCampos();
            } catch (Exception ex) {
                Logger.getLogger(PanelAgregarProductoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (!validarEntrada()) {
                Alerta.mostrarAlertaAdvertencia("Existen campos incorrectos");
            } else {
                try {
                    String codigo = txtCodigo.getText();
                    String descripcion = txtDescripcion.getText();
                    String categoria = txtCategoria.getValue();
                    String precio = txtPrecio.getText();
                    String stock = txtStock.getText();
                    Proveedor proveedor=comboProveedor.getSelectionModel().getSelectedItem();
                    DecimalFormat df = new DecimalFormat("#.##");
                    producto = new Producto();
                    producto.setCodigo(codigo);
                    producto.setDescripcion(descripcion);
                    producto.setPrecio(Double.valueOf(precio));
                    producto.setStock(Integer.valueOf(stock));
                    producto.setCategoria(categoria);
                    producto.setProveedor(proveedor);
                    productoDao.create(producto);
                    Alerta.mostrarAlertaInformacion("Carga exitosa");
                    
                    limpiarCampos();
                } catch (NumberFormatException ex) {
                    Logger.getLogger(PanelAgregarProductoController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void CancelarIngreso(ActionEvent event) {
        this.btnAgregar.getScene().getWindow().hide();
    }

    private void limpiarCampos() {
        txtCodigo.setText(null);
        txtDescripcion.setText(null);
        txtPrecio.setText(null);
        txtStock.setText(null);
    }

    @FXML
    private void stockOnKeyPressed(KeyEvent event) {
        
    }

    private boolean validarEntrada() {
        boolean flag = true;
        if (txtCodigo.getText().isEmpty()) {
            flag = false;
        }

        if (txtDescripcion.getText().isEmpty()) {
            flag = false;
        }

        if (txtPrecio.getText().isEmpty()) {
            flag = false;
        }

        if (txtStock.getText().isEmpty()) {
            flag = false;
        }

        return flag;
    }

    @FXML
    private void codigoOnKeyPressed(KeyEvent event) {
        
    }

    @FXML
    private void descripcionOnKeyPressed(KeyEvent event) {

    }

    @FXML
    private void precioOnKeyPressed(KeyEvent event) {
    }

    private void formatearEntradas() {
        txtCodigo.setTextFormatter(new TextFormatter<>(
                (change) -> {
                    String text = change.getControlNewText();
                    for (int i = 0; i < text.length(); i++) {
                        if (!text.matches("^\\w{1,10}$")) {
                            return null;
                        }
                    }
                    return change;

                }
        ));

        txtStock.setTextFormatter(new TextFormatter<>(
                (change) -> {
                    String text = change.getControlNewText();
                    for (int i = 0; i < text.length(); i++) {
                        if (!text.matches("^\\d{1,10}$")) {
                            return null;
                        }
                    }
                    return change;

                }
        ));

        txtPrecio.setTextFormatter(new TextFormatter<>(
                (change) -> {
                    String text = change.getControlNewText();
                    for (int i = 0; i < text.length(); i++) {
                        if (!text.matches("^\\d+.?[0-9]{0,2}$")) {
                            return null;
                        }
                    }
                    return change;

                }
        ));

    }
    
    private void cargarComboProveedores() {
        ObservableList<Proveedor> proveedorObs=FXCollections.observableArrayList();
        proveedorDao=new ProveedorJpaController();
        List<Proveedor> proveedores = proveedorDao.findProveedorEntities();
        for (Proveedor p : proveedores) {
            proveedorObs.add(p);
        }
        comboProveedor.setItems(proveedorObs);
    }

}
