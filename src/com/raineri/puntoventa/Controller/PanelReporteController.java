/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.raineri.puntoventa.Controller;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.raineri.puntoventa.Entity.FacturaCabezera;
import com.raineri.puntoventa.Jpa.FacturaCabezeraJpaController;
import com.raineri.puntoventa.util.Alerta;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author exera
 */
public class PanelReporteController implements Initializable {
    
    @FXML
    private TableView<FacturaCabezera> tablaReporte;
    @FXML
    private TableColumn<FacturaCabezera, String> col_id;
    @FXML
    private TableColumn<FacturaCabezera, String> col_fecha;
    @FXML
    private TableColumn<FacturaCabezera, String> col_total;
    @FXML
    private DatePicker fechaDesde;
    @FXML
    private DatePicker fechaHasta;
    
    private ObservableList<FacturaCabezera> facturas;
    private FacturaCabezeraJpaController facturaDao;
    SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yy\t\tHH:mm:ss");
    public static FacturaCabezera factura;
    Stage stage = new Stage();
    @FXML
    private Label lblEncontrados;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //configuramos la tabla
        col_id.setCellValueFactory(new PropertyValueFactory<>("nroFactura"));
        col_fecha.setCellValueFactory((param) -> {
            return new SimpleObjectProperty<>(sf.format(param.getValue().getFechaEmision()));
        });
        col_total.setCellValueFactory((param) -> {
            DecimalFormat df = new DecimalFormat("###,###.##");
            String valor = "$ " + df.format(param.getValue().getTotal());
            return new SimpleObjectProperty<>(valor);
        });
        
        listarTabla();
    }
    
    @FXML
    private void FiltrarTabla(ActionEvent event) {
        if (fechaDesde.getValue() == null || fechaHasta.getValue() == null) {
            listarTabla();
        } else {
            facturas = FXCollections.observableArrayList();
            facturaDao = new FacturaCabezeraJpaController();
            List<FacturaCabezera> facturasBD = facturaDao.findFacturaCabezeraFecha(fechaDesde.getValue(), fechaHasta.getValue());
            
            for (FacturaCabezera fc : facturasBD) {
                facturas.add(fc);
            }
            
            tablaReporte.setItems(facturas);
            lblEncontrados.setText(String.format("Encontrados(%d)", facturasBD.size()));
        }
    }
    
    @FXML
    private void VerDetalle(ActionEvent event) {
        if (tablaReporte.getSelectionModel().getSelectedItem() != null) {
            try {
                stage.close();
                facturaDao = new FacturaCabezeraJpaController();
                int idFactura = tablaReporte.getSelectionModel().getSelectedItem().getId();
                factura = facturaDao.findFacturaCabezera(idFactura);
                Parent root = FXMLLoader.load(getClass().getResource("../View/panelVerDetalle.fxml"));
                Scene scene = new Scene(root);
                
                stage.setScene(scene);
                stage.show();
                stage.setOnCloseRequest((t) -> {
                    factura = null;
                });
            } catch (IOException ex) {
                Alerta.mostrarAlertaAdvertencia("Hubo un error: " + ex.getMessage());
                Logger.getLogger(PanelReporteController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            Alerta.mostrarAlertaAdvertencia("Debe seleccionar una venta.");
        }
    }
    
    private void listarTabla() {
        facturas = FXCollections.observableArrayList();
        facturaDao = new FacturaCabezeraJpaController();
        List<FacturaCabezera> facturasBD = facturaDao.findFacturaCabezeraEntities();
        
        for (FacturaCabezera fc : facturasBD) {
            facturas.add(fc);
        }
        tablaReporte.setItems(facturas);
        lblEncontrados.setText(String.format("Encontrados(%d)", facturasBD.size()));
    }
    
    @FXML
    private void GenerarReporte(ActionEvent event) {
        try {
            if (fechaDesde.getValue() == null || fechaHasta.getValue() == null) {
                Alerta.mostrarAlertaAdvertencia("Debe seleccionar un periodo!");
            } else {
                SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yy");
                DecimalFormat df = new DecimalFormat("###,###.00");
                Document reporteCaja = new Document();
                System.out.println("size->" + reporteCaja.getPageSize().getWidth());
                PdfWriter.getInstance(reporteCaja, new FileOutputStream("Reporte_Caja/" + fechaDesde.getValue().toString() + " - " + fechaHasta.getValue() + ".pdf"));
                reporteCaja.open();
                
                Paragraph titulo = new Paragraph("Raineri Rodamientos");
                titulo.setAlignment(1);
                titulo.getFont().setSize(24);
                titulo.getFont().setStyle(3);
                Paragraph direccion = new Paragraph("Aristobulo del Valle 588");
                direccion.setAlignment(1);
                
                Paragraph fecha = new Paragraph(String.format("Reporte de caja del %10s al %10s", fechaDesde.getValue(), fechaHasta.getValue()));
                
                PdfPTable tabla = new PdfPTable(new float[]{5f, 20f, 20f, 20f});
                tabla.setWidthPercentage(100);
                PdfPCell id = new PdfPCell(new Phrase("#"));
                id.setBackgroundColor(BaseColor.LIGHT_GRAY);
                id.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(id);
                
                PdfPCell nroFactura = new PdfPCell(new Phrase("N° Factura"));
                nroFactura.setBackgroundColor(BaseColor.LIGHT_GRAY);
                nroFactura.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(nroFactura);
                
                PdfPCell importe = new PdfPCell(new Phrase("Importe"));
                importe.setBackgroundColor(BaseColor.LIGHT_GRAY);
                importe.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(importe);
                tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
                
                PdfPCell fechaEmision = new PdfPCell(new Phrase("Fecha Emisión"));
                fechaEmision.setBackgroundColor(BaseColor.LIGHT_GRAY);
                fechaEmision.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(fechaEmision);
                double total = 0.0;
                for (FacturaCabezera f : tablaReporte.getItems()) {
                    tabla.addCell(f.getId().toString());
                    tabla.addCell(f.getNroFactura());
                    tabla.addCell("$" + df.format(f.getTotal()));
                    tabla.addCell(sf.format(f.getFechaEmision()));
                    total += f.getTotal();
                }
                
                tabla.setSummary("Sumary");
                
                Paragraph parrafo_total = new Paragraph("Total: $" + df.format(total));
                parrafo_total.setAlignment(Element.ALIGN_RIGHT);
                
                reporteCaja.add(titulo);
                reporteCaja.add(direccion);
                reporteCaja.add(Chunk.NEWLINE);
                reporteCaja.add(fecha);
                reporteCaja.add(Chunk.NEWLINE);
                reporteCaja.add(tabla);
                reporteCaja.add(parrafo_total);
                reporteCaja.close();
                Alerta.mostrarAlertaInformacion("Reporte creado exitosamente");
            }
        } catch (DocumentException | FileNotFoundException ex) {
            Alerta.mostrarAlertaError("Hubo un error: " + ex.getMessage());
            Logger.getLogger(PanelReporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
