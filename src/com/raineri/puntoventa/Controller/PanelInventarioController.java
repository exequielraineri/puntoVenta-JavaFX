/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.raineri.puntoventa.Controller;

import com.raineri.puntoventa.Entity.Producto;
import com.raineri.puntoventa.Jpa.ProductoJpaController;
import com.raineri.puntoventa.Jpa.exceptions.NonexistentEntityException;
import com.raineri.puntoventa.util.Alerta;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author exera
 */
public class PanelInventarioController implements Initializable {

    @FXML
    private TableView<Producto> tablaInventario;
    @FXML
    private TableColumn<Producto, Integer> id_producto;
    @FXML
    private TableColumn<Producto, String> codigo_producto;
    @FXML
    private TableColumn<Producto, String> desc_producto;
    @FXML
    private TableColumn<Producto, Integer> stock_producto;
    @FXML
    private TableColumn<Producto, Double> precio_producto;

    private ProductoJpaController productoDao = new ProductoJpaController();
    //private ObservableList<Producto> productos;
    //private List<Producto> lista_productos;
    public static Producto productoModificar;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnNuevoProducto;

    Stage stage = new Stage();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        id_producto.setCellValueFactory(new PropertyValueFactory<>("Id"));
        desc_producto.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        codigo_producto.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        stock_producto.setCellValueFactory(new PropertyValueFactory<>("stock"));
        precio_producto.setCellValueFactory(new PropertyValueFactory<>("precio"));

        cargarTabla();
    }

    @FXML
    private void EliminarProducto(ActionEvent event) {
        if (tablaInventario.getSelectionModel().getSelectedItem() != null) {

            int idProducto = tablaInventario.getSelectionModel().getSelectedItem().getId();
            Producto producto = productoDao.findProducto(idProducto);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Importante");
            alert.setContentText("Seguro desea eliminar el producto:\n" + producto.getCodigo() + " - " + producto.getDescripcion());
            alert.setHeaderText("Eliminar Producto");
            ButtonType btnsi = new ButtonType("Si");
            ButtonType btnno = new ButtonType("No");
            alert.getButtonTypes().setAll(btnsi, btnno);

            Optional<ButtonType> showAndWait = alert.showAndWait();
            if (showAndWait.get() == btnsi) {

                try {
                    productoDao.destroy(idProducto);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(PanelInventarioController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            cargarTabla();
            alert.close();
        } else {
            Alerta.mostrarAlertaAdvertencia("Debe seleccionar un producto");
        }
    }

    @FXML
    private void nuevoProducto(ActionEvent event) {
        try {
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("../View/PanelAgregarProducto.fxml"));

            Scene scene = new Scene(root);

            stage.setTitle("Nuevo Producto");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest((t) -> {
                cargarTabla();
            });
        } catch (IOException ex) {
            Alerta.mostrarAlertaError("Hubo un error: " + ex.getMessage());
        }
    }

    public void cargarTabla() {
        tablaInventario.getItems().clear();
        ObservableList<Producto> productos = FXCollections.observableArrayList();
        productoDao = new ProductoJpaController();
        //lista_productos = productoDao.findProductoEntities();
        List<Producto> prods = productoDao.findProductoEntities();
        for (Producto p : prods) {
            productos.add(p);
        }

        tablaInventario.setItems(productos);
    }

    @FXML
    private void FiltrarBusqueda(KeyEvent event) {
        ObservableList<Producto> productos = FXCollections.observableArrayList();
        List<Producto> lista_productos = productoDao.findProducto(txtBuscar.getText());
        for (Producto p : lista_productos) {
            productos.add(p);
        }

        tablaInventario.setItems(productos);
    }

    @FXML
    private void EditarProducto(ActionEvent event) {
        if (tablaInventario.getSelectionModel().getSelectedItem() != null) {
            int idProducto = tablaInventario.getSelectionModel().getSelectedItem().getId();
            productoModificar = productoDao.findProducto(idProducto);
            try {
                stage.close();
                Parent root = FXMLLoader.load(getClass().getResource("../View/PanelAgregarProducto.fxml"));

                for (int i = 0; i < root.getChildrenUnmodifiable().size(); i++) {
                    Node get = root.getChildrenUnmodifiable().get(i);
                    if (get.getId() != null) {
                        if (get instanceof Label) {
                            Label lbl = (Label) get;
                            if (lbl.getId().equals("lblTitulo")) {
                                lbl.setText("Editar Producto");
                            }
                        }
                        if (get instanceof Button) {
                            Button btn = (Button) get;
                            if (btn.getId().equals("btnAgregar")) {
                                btn.setText("Actualizar");
                            }
                        }
                    }
                }

                Scene scene = new Scene(root);

                stage.setTitle("Editar Producto");

                stage.setScene(scene);
                stage.show();
                stage.setOnCloseRequest((t) -> {
                    productoModificar = null;
                    cargarTabla();
                });
            } catch (IOException ex) {
                Alerta.mostrarAlertaError("Hubo un error: " + ex.getMessage());
            }
        } else {
            Alerta.mostrarAlertaAdvertencia("Debe seleccionar un producto!");
        }

    }
}
