/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.raineri.puntoventa.Controller;

import com.raineri.puntoventa.Entity.FacturaCabezera;
import com.raineri.puntoventa.Entity.FacturaDetalle;
import com.raineri.puntoventa.Entity.Producto;
import com.raineri.puntoventa.Jpa.FacturaCabezeraJpaController;
import com.raineri.puntoventa.Jpa.FacturaDetalleJpaController;
import com.raineri.puntoventa.Jpa.ProductoJpaController;
import com.raineri.puntoventa.util.Alerta;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.raineri.puntoventa.App;
import java.io.FileOutputStream;

/**
 * FXML Controller class
 *
 * @author exera
 */
public class PanelVentaController implements Initializable {

    @FXML
    private TableView<Producto> tablaDetalle;
    @FXML
    private TableColumn<Producto, Integer> col_id;
    @FXML
    private TableColumn<Producto, String> col_codigo;
    @FXML
    private TableColumn<Producto, String> col_descripcion;
    @FXML
    private TableColumn<Producto, Integer> col_cantidad;
    @FXML
    private TableColumn<Producto, Double> col_precio;
    @FXML
    private TableColumn<Producto, Double> col_subtotal;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtStock;
    @FXML
    private TextField txtTotal;

    @FXML
    private TextField txtBusqueda;
    @FXML
    private ComboBox<Producto> comboBoxFiltrado;

    private List<Producto> productosComboBox = new ArrayList<>();
    private ObservableList<Producto> productosDetalle = FXCollections.observableArrayList();
    Producto p;
    ObservableList<Producto> productosObserv;
    Double total;

    ProductoJpaController pDao;
    FacturaCabezeraJpaController cabezeraDao;
    FacturaDetalleJpaController detalleDao;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        productosObserv = FXCollections.observableArrayList();
        txtBusqueda.setOnKeyPressed((event) -> {
            productosComboBox = cargarTablaConFiltro(event);
            productosObserv.setAll(productosComboBox);

            comboBoxFiltrado.setItems(productosObserv);
            comboBoxFiltrado.getSelectionModel().selectFirst();
        });

        comboBoxFiltrado.setOnAction((event) -> {

            if (comboBoxFiltrado.getSelectionModel().getSelectedItem() != null) {
                p = comboBoxFiltrado.getSelectionModel().getSelectedItem();
                txtDescripcion.setText(p.getDescripcion());
                txtPrecio.setText(p.getPrecio().toString());
                txtStock.setText(p.getStock().toString());

            } else {
                txtDescripcion.setText(null);
                txtPrecio.setText(null);
                txtStock.setText(null);
            }
        });

        //configuramos la tabla
        col_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        col_codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        col_descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        col_precio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        col_cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        col_subtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

    }

    @FXML
    private void AgregarDetalle(ActionEvent event) {
        total = 0.0;
        if (productosDetalle.contains(p)) {

            for (int i = 0; i < productosDetalle.size(); i++) {
                if (productosDetalle.get(i).getId().intValue() == p.getId().intValue()) {

                    productosDetalle.get(i).setCantidad(productosDetalle.get(i).getCantidad() + Integer.valueOf(txtCantidad.getText()));
                    productosDetalle.set(i, productosDetalle.get(i));

                }
            }

        } else {
            p.setCantidad(Integer.valueOf(txtCantidad.getText()));
            productosDetalle.add(p);
        }

        tablaDetalle.setItems(productosDetalle);

        for (Producto prod : productosDetalle) {
            total += prod.getSubtotal();
        }

        txtTotal.setText("$" + total);

    }

    @FXML
    private void EliminarDetalleSeleccionado(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Seguro desea eliminar?");
        ButtonType btnsi = new ButtonType("Si");
        ButtonType btnno = new ButtonType("No");
        alert.getButtonTypes().setAll(btnsi, btnno);
        Optional<ButtonType> showAndWait = alert.showAndWait();
        if (showAndWait.get() == btnsi) {

            Producto pEliminar = tablaDetalle.getSelectionModel().getSelectedItem();
            productosDetalle.remove(pEliminar);
            total = 0.0;
            tablaDetalle.setItems(productosDetalle);
            for (Producto prod : productosDetalle) {
                total += prod.getSubtotal();

            }

            txtTotal.setText("$" + total);
        }
    }

    private List<Producto> cargarTablaConFiltro(KeyEvent event) {
        productosComboBox = new ArrayList<>();
        pDao = new ProductoJpaController();
        return pDao.findProducto(event.getText());

    }

    @FXML
    private void RealizarVenta(ActionEvent event) {
        if (tablaDetalle.getItems().isEmpty()) {
            Alerta.mostrarAlertaAdvertencia("No agregó ningun producto!");
        } else {
            try {
                cabezeraDao = new FacturaCabezeraJpaController();
                detalleDao = new FacturaDetalleJpaController();
                FacturaCabezera facturaCabezera = new FacturaCabezera();
                facturaCabezera = cabezeraDao.create(facturaCabezera);
                List<FacturaDetalle> detalles = new ArrayList<>();
                FacturaDetalle detalle;
                for (Producto producto : productosDetalle) {
                    detalle = new FacturaDetalle();
                    detalle.setIdFacCabezera(facturaCabezera);
                    detalle.setIdProducto(producto);
                    detalle.setCantidad(producto.getCantidad());
                    detalle.setSubtotal(producto.getSubtotal());
                    detalle=detalleDao.create(detalle);
                    
                    producto.setStock(producto.getStock() - producto.getCantidad());
                    
                    detalles.add(detalle);
                    producto.getFacturaDetalleList().add(detalle);
                    pDao.edit(producto);
                }
                facturaCabezera.setFacturaDetalleList(detalles);
                facturaCabezera.setFechaEmision(new Date());
                DecimalFormat sf = new DecimalFormat("00000");
                facturaCabezera.setNroFactura(sf.format(facturaCabezera.getId()));
                cabezeraDao.edit(facturaCabezera);

                //generarTicketPDF(facturaCabezera);
                Alerta.mostrarAlertaInformacion("Factura crada exitosamente!");
                LimpiarCampos(event);
            } catch (Exception ex) {
                Alerta.mostrarAlertaError("Hubo un error: " + ex.getMessage());
                Logger.getLogger(PanelVentaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void LimpiarCampos(ActionEvent event) {
        txtBusqueda.setText(null);
        productosDetalle = FXCollections.observableArrayList();
        txtCantidad.setText(null);
        txtDescripcion.setText(null);
        txtPrecio.setText(null);
        txtStock.setText(null);
        txtTotal.setText(null);
        total = 0.0;
        p = new Producto();
        tablaDetalle.setItems(productosDetalle);
        productosObserv = FXCollections.observableArrayList();
        comboBoxFiltrado.setItems(productosObserv);
    }

    private void generarTicketPDF(FacturaCabezera facturaCabezera) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Document ticket = new Document();
            PdfWriter.getInstance(ticket, new FileOutputStream(facturaCabezera.getNroFactura().concat("_Ticket.pdf")));
            ticket.open();
            ticket.setPageSize(new Rectangle(200, 600));
            Paragraph parrafo = new Paragraph();
            parrafo.setAlignment(1);
            
            parrafo.add("****************************");
            parrafo.add("\nRaineri Rodamientos");
            parrafo.add("\nAristobulo del Valle 588");
            parrafo.add("\nFecha y Hora: " + sf.format(new Date()));
            parrafo.add("\n****************************");

            PdfPTable tabla = new PdfPTable(4);
            tabla.addCell("Codigo");
            tabla.addCell("Descripción");
            tabla.addCell("Cant.");
            tabla.addCell("Importe");
            tabla.setHeaderRows(0);
            double total = 0.0;
            for (Producto p : productosDetalle) {
                tabla.addCell(p.getCodigo());
                tabla.addCell(p.getDescripcion());
                tabla.addCell(p.getCantidad().toString());
                tabla.addCell("$ " + p.getSubtotal().toString());
                total += p.getSubtotal();
            }

            Paragraph Total = new Paragraph("$ " + String.valueOf(total));
            Total.setAlignment(3);
            ticket.add(parrafo);
            ticket.add(tabla);
            ticket.add(Total);
            ticket.close();
            /*File ticket=new File(facturaCabezera.getNroFactura()+"_Ticket");
            FileWriter wr=new FileWriter(ticket);
            SimpleDateFormat sf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            wr.append("*****************************");
            wr.append("Raineri Rodamientos");
            wr.append("Aristobulo del Valle 588");
            wr.append("Fecha y Hora: "+sf.format(new Date()));
            wr.append("*****************************");
            wr.write("hola");
            wr.close();*/
        } catch (IOException ex) {
            Logger.getLogger(PanelVentaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(PanelVentaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
