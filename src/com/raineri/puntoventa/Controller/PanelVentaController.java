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
import com.raineri.puntoventa.Entity.Cliente;
import com.raineri.puntoventa.Jpa.ClienteJpaController;
import java.io.FileOutputStream;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

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
    @FXML
    private RadioButton RB_efectivo;
    @FXML
    private ToggleGroup pago;
    @FXML
    private RadioButton RB_transferencia;
    @FXML
    private RadioButton RB_cheque;
    @FXML
    private TextField lblSubtotal;
    @FXML
    private ComboBox<Double> comboIva;
    @FXML
    private TextField txtCliente;
    @FXML
    private ComboBox<Cliente> comboBoxClientes;

    private List<Producto> productosLista = new ArrayList<>();
    private ObservableList<Producto> productosDetalleObserv = FXCollections.observableArrayList();
    private Producto producto;
    private Cliente cliente;
    private ObservableList<Producto> productosObserv;
    private Double total;
    private Double subtotal;

    DecimalFormat df = new DecimalFormat("###,##0.00");

    private ClienteJpaController clienteDao;
    private ProductoJpaController productoDao;
    private FacturaCabezeraJpaController facturaCabezeraDao;
    private FacturaDetalleJpaController facturaDetalleDao;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        productosObserv = FXCollections.observableArrayList();
        cargarComboBox();
        cargarComboIva();
        txtBusqueda.setOnKeyPressed((event) -> {
            productosLista = cargarTablaConFiltro(event);
            productosObserv.setAll(productosLista);

            comboBoxFiltrado.setItems(productosObserv);
            comboBoxFiltrado.getSelectionModel().selectFirst();
        });

        txtCliente.setOnKeyPressed((event) -> {
            String text = txtCantidad.getText().concat(event.getText());
            ObservableList<Cliente> clienteObserv = FXCollections.observableArrayList();
            List<Cliente> clientesComboBox = cargarClientesConFiltro(text);
            clienteObserv.setAll(clientesComboBox);

            comboBoxClientes.setItems(clienteObserv);
            comboBoxClientes.getSelectionModel().selectFirst();
        });

        txtCantidad.setDisable(true);
        comboBoxFiltrado.setOnAction((event) -> {

            if (comboBoxFiltrado.getSelectionModel().getSelectedItem() != null) {
                producto = comboBoxFiltrado.getSelectionModel().getSelectedItem();
                txtDescripcion.setText(producto.getDescripcion());
                txtPrecio.setText(df.format(producto.getPrecio().doubleValue()));
                txtStock.setText(producto.getStock().toString());
                txtCantidad.setDisable(false);
            } else {
                txtDescripcion.setText(null);
                txtPrecio.setText(null);
                txtStock.setText(null);
                txtCantidad.setDisable(true);
                txtCantidad.setText(null);
            }
        });

        //configuramos la tabla para ingresar los valores que tendra
        col_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        col_codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        col_descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        col_precio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        col_cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        col_subtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        //asignamos el ancho de las celdas
        col_id.prefWidthProperty().bind(tablaDetalle.widthProperty().divide(6)); // w * 1/5
        col_cantidad.prefWidthProperty().bind(tablaDetalle.widthProperty().divide(6)); // w * 1/5
        col_codigo.prefWidthProperty().bind(tablaDetalle.widthProperty().divide(6)); // w * 1/5
        col_descripcion.prefWidthProperty().bind(tablaDetalle.widthProperty().divide(6)); // w * 1/5
        col_precio.prefWidthProperty().bind(tablaDetalle.widthProperty().divide(6)); // w * 1/5
        col_subtotal.prefWidthProperty().bind(tablaDetalle.widthProperty().divide(6)); // w * 1/5

        //formateamos la entrada que solo reciba enteros
        TextFormatter formatoSoloEnteros = new TextFormatter<>(
                (change) -> {
                    String text = change.getControlNewText();

                    for (int i = 0; i < text.length(); i++) {
                        if (!text.matches("^\\d{1,}$")) {
                            return null;
                        }
                        if ((text == "" ? Integer.parseInt("0") : Integer.parseInt(text)) <= 0) {
                            return null;
                        }
                    }
                    return change;

                }
        );

        txtCantidad.setTextFormatter(formatoSoloEnteros);

        //verificamos que la cantidad que digite sea menor al stock 
        txtCantidad.setOnKeyReleased((event) -> {
            int cant = txtCantidad.getText() == null ? 0 : Integer.parseInt(txtCantidad.getText());
            if (cant > Integer.parseInt(txtStock.getText())) {
                Alerta.mostrarAlertaAdvertencia("Supera el stock");
                txtCantidad.setText(null);
            }
        });

        comboIva.setOnAction((event) -> {
            calcularTotal();
        });

        comboIva.getSelectionModel().selectFirst();
    }

    private void calcularTotal() {
        if (comboIva.getSelectionModel().getSelectedItem() != null) {
            switch (comboIva.getSelectionModel().getSelectedItem().toString()) {
                case "10.5": {
                    total = (subtotal * 10.5) / 100 + subtotal;
                    break;
                }
                case "21.0": {
                    total = (subtotal * 21.0) / 100 + subtotal;
                    break;
                }
                default: {
                    total = subtotal;
                }
            }
            txtTotal.setText("$ " + df.format(total));
        } else {
            txtTotal.setText("$ " + df.format(subtotal));
        }
    }

    @FXML
    private void AgregarDetalle(ActionEvent event) {
        subtotal = 0.0;
        if (productosDetalleObserv.contains(producto)) {

            for (int i = 0; i < productosDetalleObserv.size(); i++) {
                if (productosDetalleObserv.get(i).getId().intValue() == producto.getId().intValue()) {

                    productosDetalleObserv.get(i).setCantidad(productosDetalleObserv.get(i).getCantidad() + Integer.valueOf(txtCantidad.getText()));
                    productosDetalleObserv.set(i, productosDetalleObserv.get(i));

                }
            }

        } else {
            producto.setCantidad(Integer.valueOf(txtCantidad.getText()));
            productosDetalleObserv.add(producto);
        }

        tablaDetalle.setItems(productosDetalleObserv);

        for (Producto prod : productosDetalleObserv) {
            subtotal += prod.getSubtotal();
        }

        lblSubtotal.setText("$" + df.format(subtotal));
        txtTotal.setText("$ " + df.format(subtotal));
        calcularTotal();
    }

    @FXML
    private void EliminarDetalleSeleccionado(ActionEvent event) {
        if (Alerta.mostrarAlertaConfirmation("Seguro desea eliminar?")) {
            Producto pEliminar = tablaDetalle.getSelectionModel().getSelectedItem();
            productosDetalleObserv.remove(pEliminar);
            subtotal = 0.0;
            tablaDetalle.setItems(productosDetalleObserv);
            for (Producto prod : productosDetalleObserv) {
                subtotal += prod.getSubtotal();

            }
            lblSubtotal.setText("$" + df.format(subtotal));
            txtTotal.setText("$ " + df.format(subtotal));
            calcularTotal();
        }
    }

    private List<Producto> cargarTablaConFiltro(KeyEvent event) {
        productosLista = new ArrayList<>();
        productoDao = new ProductoJpaController();
        return productoDao.findProducto(event.getText());

    }

    @FXML
    private void RealizarVenta(ActionEvent event) {
        if (tablaDetalle.getItems().isEmpty()) {
            Alerta.mostrarAlertaAdvertencia("No agregó ningun producto!");
        } else {
            try {
                if (comboBoxClientes.getSelectionModel().getSelectedItem() != null) {
                    cliente = comboBoxClientes.getSelectionModel().getSelectedItem();
                    facturaCabezeraDao = new FacturaCabezeraJpaController();
                    facturaDetalleDao = new FacturaDetalleJpaController();
                    FacturaCabezera facturaCabezera = new FacturaCabezera();
                    facturaCabezera = facturaCabezeraDao.create(facturaCabezera);
                    List<FacturaDetalle> detalles = new ArrayList<>();
                    FacturaDetalle detalle;
                    for (Producto producto : productosDetalleObserv) {
                        detalle = new FacturaDetalle();
                        detalle.setIdFacCabezera(facturaCabezera);
                        detalle.setIdProducto(producto);
                        detalle.setCantidad(producto.getCantidad());
                        detalle.setSubtotal(producto.getSubtotal());
                        detalle = facturaDetalleDao.create(detalle);

                        producto.setStock(producto.getStock() - producto.getCantidad());

                        detalles.add(detalle);
                        producto.getFacturaDetalleList().add(detalle);
                        productoDao.edit(producto);
                    }
                    facturaCabezera.setFacturaDetalleList(detalles);
                    facturaCabezera.setFechaEmision(new Date());
                    DecimalFormat sf = new DecimalFormat("0000");
                    facturaCabezera.setNroFactura(sf.format(facturaCabezera.getId()));
                    facturaCabezera.setIdCliente(cliente);
                    if (RB_efectivo.isSelected()) {
                        facturaCabezera.setMetodoPago("Efectivo");
                    } else if (RB_transferencia.isSelected()) {
                        facturaCabezera.setMetodoPago("Transferencia");
                    } else {
                        facturaCabezera.setMetodoPago("Cheque");
                    }

                    Double iva = comboIva.getSelectionModel().getSelectedItem();
                    facturaCabezera.setIva(iva);
                    facturaCabezeraDao.edit(facturaCabezera);

                    //generarTicketPDF(facturaCabezera);
                    Alerta.mostrarAlertaInformacion("Factura crada exitosamente!");
                    LimpiarCampos(event);
                    cliente = null;
                } else {
                    Alerta.mostrarAlertaAdvertencia("Debe seleccionar un cliente");
                }
            } catch (Exception ex) {
                Alerta.mostrarAlertaError("Hubo un error: " + ex.getMessage());
                Logger.getLogger(PanelVentaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void LimpiarCampos(ActionEvent event) {
        txtBusqueda.setText(null);
        productosDetalleObserv = FXCollections.observableArrayList();
        txtCantidad.setText(null);
        txtDescripcion.setText(null);
        txtPrecio.setText(null);
        txtStock.setText(null);
        txtTotal.setText(null);
        total = 0.0;
        subtotal = 0.0;
        producto = new Producto();
        tablaDetalle.setItems(productosDetalleObserv);
        cargarComboBox();
        lblSubtotal.setText(null);
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
            for (Producto p : productosDetalleObserv) {
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
        } catch (IOException | DocumentException ex) {
            Logger.getLogger(PanelVentaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void cargarComboBox() {
        productoDao = new ProductoJpaController();
        comboBoxFiltrado.getItems().clear();
        productosObserv = FXCollections.observableArrayList();
        productosLista = productoDao.findProductoEntities();
        for (Producto producto : productosLista) {
            productosObserv.add(producto);
        }
        comboBoxFiltrado.setItems(productosObserv);

        clienteDao = new ClienteJpaController();
        comboBoxClientes.getItems().clear();
        ObservableList<Cliente> clienteObsv = FXCollections.observableArrayList();
        List<Cliente> listaClientes = clienteDao.findClienteEntities();
        for (Cliente cliente : listaClientes) {
            clienteObsv.add(cliente);
        }
        comboBoxClientes.setItems(clienteObsv);
    }

    private void cargarComboIva() {
        comboIva.getItems().clear();
        comboIva.getItems().addAll(0.00, 10.5, 21.0);
    }

    private List<Cliente> cargarClientesConFiltro(String txt) {
        List<Cliente> clienteList = new ArrayList<>();
        clienteDao = new ClienteJpaController();
        return clienteDao.findClienteEntitiesFiltro(txt);

    }

}
