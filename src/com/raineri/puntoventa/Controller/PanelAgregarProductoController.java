/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.raineri.puntoventa.Controller;

import static com.raineri.puntoventa.Controller.PanelInventarioController.productoModificar;
import com.raineri.puntoventa.Entity.Producto;
import com.raineri.puntoventa.Jpa.ProductoJpaController;
import com.raineri.puntoventa.util.Alerta;
import java.net.URL;
import java.text.DecimalFormat;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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

    String caracteres = "0123456789";
    String caracteresFloat = "0123456789,.";

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

        txtCategoria.setItems(categorias);
        if (productoModificar != null) {
            txtCodigo.setText(productoModificar.getCodigo());
            txtDescripcion.setText(productoModificar.getDescripcion());
            txtPrecio.setText(productoModificar.getPrecio().toString());
            txtStock.setText(productoModificar.getStock().toString());
            txtCategoria.getSelectionModel().select(productoModificar.getCategoria());
        }

    }

    @FXML
    private void guardarProducto(ActionEvent event) {
        if (productoModificar != null) {
            try {
                productoModificar.setDescripcion(txtDescripcion.getText());
                productoModificar.setCodigo(txtCodigo.getText());
                productoModificar.setCategoria(txtCategoria.getValue());
                productoModificar.setStock(Integer.valueOf(txtStock.getText()));
                productoModificar.setPrecio(Double.valueOf(txtPrecio.getText()));
                productoDao.edit(productoModificar);
                Alerta.mostrarAlertaInformacion("Se modifico el producto exitosamente!");
                limpiarCampos();

            } catch (Exception ex) {
                Alerta.mostrarAlertaAdvertencia("Hubo un error: " + ex.getMessage());
                Logger.getLogger(PanelAgregarProductoController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            if (!validarEntrada()) {
                Alerta.mostrarAlertaAdvertencia("Existen campos incorrectos");

            } else {
                String codigo = txtCodigo.getText();
                String descripcion = txtDescripcion.getText();
                String categoria = txtCategoria.getValue();
                String precio = txtPrecio.getText();
                String stock = txtStock.getText();

                DecimalFormat df = new DecimalFormat("#.##");
                producto = new Producto();
                producto.setCodigo(codigo);
                producto.setDescripcion(descripcion);
                producto.setPrecio(Double.valueOf(precio));
                producto.setStock(Integer.valueOf(stock));
                producto.setCategoria(categoria);

                productoDao.create(producto);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Ingreso");
                alert.setContentText("Se cargo un nuevo producto en la base de datos.");
                alert.setHeaderText("Carga Exitosa!");
                alert.showAndWait();

                limpiarCampos();
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

    @FXML
    private void tipeado(KeyEvent event) {

    }

    @FXML
    private void tttttt(ActionEvent event) {

    }

    private boolean validarEntrada() {
        boolean flag = true;
        if (txtCodigo.getText().isEmpty()) {
            flag = false;
            txtCodigo.setStyle("-fx-border-color:red");
        } else {

            txtCodigo.setStyle("-fx-border-color:lightblue");
        }

        if (txtDescripcion.getText().isEmpty()) {
            flag = false;
            txtDescripcion.setStyle("-fx-border-color:red");
        } else {

            txtDescripcion.setStyle("-fx-border-color:lightblue");
        }

        if (txtPrecio.getText().isEmpty()) {
            flag = false;
            txtPrecio.setStyle("-fx-border-color:red");
        } else {

            txtPrecio.setStyle("-fx-border-color:lightblue");
        }

        if (txtStock.getText().isEmpty()) {
            flag = false;
            txtStock.setStyle("-fx-border-color:red");
        } else {

            txtStock.setStyle("-fx-border-color:lightblue");
        }

        return flag;
    }

}
