/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.raineri.puntoventa.Controller;

import com.raineri.puntoventa.Entity.FacturaCabezera;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.raineri.puntoventa.Controller.PanelReporteController;
import static com.raineri.puntoventa.Controller.PanelReporteController.factura;
import com.raineri.puntoventa.Entity.FacturaDetalle;
import com.raineri.puntoventa.Entity.Producto;
import com.raineri.puntoventa.Jpa.FacturaDetalleJpaController;
import java.text.DecimalFormat;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author exera
 */
public class PanelVerDetalleController implements Initializable {

    @FXML
    private Label lblNroFactura;
    @FXML
    private Label lblFechaYHora;
    @FXML
    private TableView<FacturaDetalle> tablaDetalle;
    @FXML
    private TableColumn<FacturaDetalle, Integer> col_id;
    @FXML
    private TableColumn<FacturaDetalle, String> col_codigo;
    @FXML
    private TableColumn<FacturaDetalle, String> col_descripcion;
    @FXML
    private TableColumn<FacturaDetalle, Integer> col_cantidad;
    @FXML
    private TableColumn<FacturaDetalle, String> col_subtotal;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblNombreyApellido;
    @FXML
    private Label lblCuit;
    @FXML
    private Label lblDireccion;
    
    
    
    
    FacturaDetalleJpaController detalleDao = new FacturaDetalleJpaController();
    SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    DecimalFormat df=new DecimalFormat("###,##0.00");
    ObservableList<FacturaDetalle> detalle;
    List<FacturaDetalle> lista_detalle;

    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        //Cargo los datos de la factura
        lblFechaYHora.setText("Fecha y Hora: "+sf.format(new Date()));
        lblNroFactura.setText("N° Factura: "+factura.getNroFactura());
        lblTotal.setText("Total: $ "+df.format(factura.getTotal()));
        lblCuit.setText(factura.getIdCliente().getCuit());
        lblNombreyApellido.setText(String.format("%s, %s", factura.getIdCliente().getApellido(),factura.getIdCliente().getNombre()));
        lblDireccion.setText(factura.getIdCliente().getDomicilio());
        
        
        //Asigno valores a las celdas
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_codigo.setCellValueFactory((param) -> {
            return new SimpleObjectProperty<>(param.getValue().getIdProducto().getCodigo());
        });
        col_descripcion.setCellValueFactory((param) -> {
            return new SimpleObjectProperty<>(param.getValue().getIdProducto().getDescripcion());
        });
        col_cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        col_subtotal.setCellValueFactory((param) -> {
            return new SimpleObjectProperty<>("$"+df.format(param.getValue().getSubtotal()));
        });

        
        //Asigno el ancho de cada celda
        col_id.prefWidthProperty().bind(tablaDetalle.widthProperty().divide(5)); // w * 1/5
        col_cantidad.prefWidthProperty().bind(tablaDetalle.widthProperty().divide(5)); // w * 1/5
        col_codigo.prefWidthProperty().bind(tablaDetalle.widthProperty().divide(5)); // w * 1/5
        col_descripcion.prefWidthProperty().bind(tablaDetalle.widthProperty().divide(5)); // w * 1/5
        col_subtotal.prefWidthProperty().bind(tablaDetalle.widthProperty().divide(5)); // w * 1/5
        
        cargarTabla();
    }

    private void cargarTabla() {
        tablaDetalle.getItems().clear();
        detalle = FXCollections.observableArrayList();
        lista_detalle = detalleDao.findFacturaDetalleEntities();

        for (FacturaDetalle fd : lista_detalle) {
            if (fd.getIdFacCabezera().equals(factura)) {
                detalle.add(fd);
            }
        }
        
        tablaDetalle.setItems(detalle);
    }

}
