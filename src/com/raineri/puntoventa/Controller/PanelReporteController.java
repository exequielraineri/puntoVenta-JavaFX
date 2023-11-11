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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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
    public static FacturaCabezera factura;
    Stage stage = new Stage();
    @FXML
    private Label lblEncontrados;
    @FXML
    private TableColumn<FacturaCabezera, String> col_metodoPago;
    @FXML
    private TableColumn<FacturaCabezera, String> col_hora;
    @FXML
    private ComboBox<String> comboBoxMetodoPago;
    @FXML
    private Label lblEfectivo;
    @FXML
    private Label lbl1transferencia;
    @FXML
    private Label lblCheque;

    private double totalEfectivo;
    private double totalTransferencia;
    private double totalCheque;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //configuramos la tabla

        col_id.setCellValueFactory(new PropertyValueFactory<>("nroFactura"));
        col_fecha.setCellValueFactory((param) -> {
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yy");
            return new SimpleObjectProperty<>(sf.format(param.getValue().getFechaEmision()));
        });

        col_hora.setCellValueFactory((param) -> {
            SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
            return new SimpleObjectProperty<>(sf.format(param.getValue().getFechaEmision()));
        });
        col_total.setCellValueFactory((param) -> {
            DecimalFormat df = new DecimalFormat("###,###.00");
            String valor = "$ " + df.format(param.getValue().getTotal());
            return new SimpleObjectProperty<>(valor);
        });

        col_metodoPago.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));

        col_id.prefWidthProperty().bind(tablaReporte.widthProperty().divide(5)); // w * 1/5
        col_fecha.prefWidthProperty().bind(tablaReporte.widthProperty().divide(5)); // w * 1/5
        col_metodoPago.prefWidthProperty().bind(tablaReporte.widthProperty().divide(5)); // w * 1/5
        col_hora.prefWidthProperty().bind(tablaReporte.widthProperty().divide(5)); // w * 1/5
        col_total.prefWidthProperty().bind(tablaReporte.widthProperty().divide(5)); // w * 1/5

        comboBoxMetodoPago.getItems().addAll("Todos", "Efectivo", "Transferencia", "Cheque");
        listarTabla();
    }

    @FXML
    private void FiltrarTabla(ActionEvent event) {
        totalCheque = 0;
        totalEfectivo = 0;
        totalTransferencia = 0;
        facturaDao = new FacturaCabezeraJpaController();

        if (fechaDesde.getValue() == null || fechaHasta.getValue() == null) {
            if (comboBoxMetodoPago.getSelectionModel().getSelectedItem() != null) {
                listarTabla(comboBoxMetodoPago.getSelectionModel().getSelectedItem());
            } else {
                listarTabla();
            }

        } else {
            List<FacturaCabezera> facturasBD = null;
            if (comboBoxMetodoPago.getSelectionModel().getSelectedItem() != null) {
                facturasBD = facturaDao.findFacturaCabezeraFechaYMetodo(fechaDesde.getValue(), fechaHasta.getValue(), comboBoxMetodoPago.getSelectionModel().getSelectedItem());
            } else {
                facturasBD = facturaDao.findFacturaCabezeraEntities();
            }
            facturas = FXCollections.observableArrayList();
            
            for (FacturaCabezera fc : facturasBD) {
                facturas.add(fc);
                switch (fc.getMetodoPago()) {
                    case "Efectivo": {
                        totalEfectivo += fc.getTotal();
                        break;
                    }
                    case "Transferencia": {
                        totalTransferencia += fc.getTotal();
                        break;
                    }
                    case "Cheque": {
                        totalCheque += fc.getTotal();
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }

            tablaReporte.setItems(facturas);
            lblEncontrados.setText(String.format("Encontrados(%d)", facturasBD.size()));
            lblEfectivo.setText(String.format("Efectivo: $ %.2f", totalEfectivo));
            lblCheque.setText(String.format("Cheque: $ %.2f", totalCheque));
            lbl1transferencia.setText(String.format("Transferencia: $ %.2f", totalTransferencia));
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
        totalCheque = 0;
        totalEfectivo = 0;
        totalTransferencia = 0;
        facturas = FXCollections.observableArrayList();
        facturaDao = new FacturaCabezeraJpaController();
        List<FacturaCabezera> facturasBD = facturaDao.findFacturaCabezeraEntities();

        for (FacturaCabezera fc : facturasBD) {
            facturas.add(fc);
            switch (fc.getMetodoPago()) {
                case "Efectivo": {
                    totalEfectivo += fc.getTotal();
                    break;
                }
                case "Transferencia": {
                    totalTransferencia += fc.getTotal();
                    break;
                }
                case "Cheque": {
                    totalCheque += fc.getTotal();
                    break;
                }
                default: {
                    break;
                }
            }
        }
        tablaReporte.setItems(facturas);
        lblEncontrados.setText(String.format("Encontrados(%d)", facturasBD.size()));
        lblEfectivo.setText(String.format("Efectivo: $ %.2f", totalEfectivo));
        lblCheque.setText(String.format("Cheque: $ %.2f", totalCheque));
        lbl1transferencia.setText(String.format("Transferencia: $ %.2f", totalTransferencia));
    }

    @FXML
    private void GenerarReporte(ActionEvent event) {
        try {
            if (fechaDesde.getValue() == null || fechaHasta.getValue() == null) {
                Alerta.mostrarAlertaAdvertencia("Debe seleccionar un periodo!");
            } else {
                int cantCheque = 0;
                int cantEfectivo = 0;
                int cantTransferencia = 0;
                SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yy HH:mm");
                DecimalFormat df = new DecimalFormat("###,##0.00");
                Document reporteCaja = new Document();
                PdfWriter.getInstance(reporteCaja, new FileOutputStream("Reporte_Caja/" + fechaDesde.getValue().toString() + " - " + fechaHasta.getValue() + ".pdf"));
                reporteCaja.open();

                Paragraph titulo = new Paragraph("Raineri Rodamientos");
                titulo.setAlignment(1);
                titulo.getFont().setSize(24);
                titulo.getFont().setStyle(3);
                Paragraph direccion = new Paragraph("Aristobulo del Valle 588");
                direccion.setAlignment(1);
                Paragraph fechaActual = new Paragraph(sf.format(new Date()));
                direccion.setAlignment(1);

                Paragraph fecha = new Paragraph(String.format("Reporte de caja del %10s al %10s", fechaDesde.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), fechaHasta.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

                PdfPTable tabla = new PdfPTable(new float[]{5f, 20f, 20f, 20f, 20f});
                tabla.setWidthPercentage(100);

                PdfPCell id = new PdfPCell(new Phrase("#"));
                id.setBackgroundColor(BaseColor.LIGHT_GRAY);
                id.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(id);

                PdfPCell fechaEmision = new PdfPCell(new Phrase("Fecha Emisión"));
                fechaEmision.setBackgroundColor(BaseColor.LIGHT_GRAY);
                fechaEmision.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(fechaEmision);

                PdfPCell nroFactura = new PdfPCell(new Phrase("N° Factura"));
                nroFactura.setBackgroundColor(BaseColor.LIGHT_GRAY);
                nroFactura.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(nroFactura);

                PdfPCell metodoPago = new PdfPCell(new Phrase("Metodo Pago"));
                metodoPago.setBackgroundColor(BaseColor.LIGHT_GRAY);
                metodoPago.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(metodoPago);

                PdfPCell importe = new PdfPCell(new Phrase("Importe"));
                importe.setBackgroundColor(BaseColor.LIGHT_GRAY);
                importe.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.addCell(importe);

                tabla.setHorizontalAlignment(Element.ALIGN_CENTER);

                double total = 0.0;
                for (FacturaCabezera f : tablaReporte.getItems()) {
                    tabla.addCell(f.getId().toString());
                    tabla.addCell(sf.format(f.getFechaEmision()));
                    tabla.addCell(f.getNroFactura());
                    tabla.addCell(f.getMetodoPago());
                    tabla.addCell("$ " + df.format(f.getTotal()));
                    switch (f.getMetodoPago()) {
                        case "Efectivo": {
                            cantEfectivo++;
                            break;
                        }
                        case "Transferencia": {
                            cantTransferencia++;
                            break;
                        }
                        case "Cheque": {
                            cantCheque++;
                            break;
                        }
                        default: {
                            break;
                        }

                    }
                    total += f.getTotal();
                }

                tabla.setSummary("Sumary");

                //Paragraph parrafo_total = new Paragraph("Total: $" + df.format(total));
                //parrafo_total.setAlignment(Element.ALIGN_RIGHT);
                PdfPTable tablaTotalParrafo = new PdfPTable(new float[]{5f, 20f, 20f, 20f, 20f});
                tablaTotalParrafo.setWidthPercentage(100);
                tablaTotalParrafo.getDefaultCell().setBorder(0);
                tablaTotalParrafo.addCell("");
                tablaTotalParrafo.addCell("");
                tablaTotalParrafo.addCell("");
                tablaTotalParrafo.addCell(String.format("Total (%d):", (cantCheque + cantEfectivo + cantTransferencia)));
                tablaTotalParrafo.addCell(String.format("$ %s", df.format(total)));

                PdfPTable tablaTotales = new PdfPTable(3);
                tablaTotales.getDefaultCell().setBorder(0);
                PdfPCell efect = new PdfPCell(new Phrase(String.format("Efectivo (%d): $ %s", cantEfectivo, df.format(totalEfectivo))));
                efect.setBorder(Rectangle.LEFT);

                PdfPCell chequ = new PdfPCell(new Phrase(String.format("Cheque (%d): $ %s", cantCheque, df.format(totalCheque))));
                chequ.setBorder(Rectangle.LEFT);

                PdfPCell transfer = new PdfPCell(new Phrase(String.format("Transferencia (%d): $ %s", cantTransferencia, df.format(totalTransferencia))));
                transfer.setBorder(Rectangle.LEFT);

                tablaTotales.addCell(efect);
                tablaTotales.addCell(chequ);
                tablaTotales.addCell(transfer);

                reporteCaja.add(titulo);
                reporteCaja.add(direccion);
                reporteCaja.add(fechaActual);
                reporteCaja.add(Chunk.NEWLINE);
                reporteCaja.add(fecha);
                reporteCaja.add(Chunk.NEWLINE);
                reporteCaja.add(tabla);
                reporteCaja.add(tablaTotalParrafo);
                reporteCaja.add(Chunk.NEWLINE);
                reporteCaja.add(tablaTotales);
                reporteCaja.close();
                Alerta.mostrarAlertaInformacion("Reporte creado exitosamente");
            }
        } catch (DocumentException | FileNotFoundException ex) {
            Alerta.mostrarAlertaError("Hubo un error: " + ex.getMessage());
            Logger.getLogger(PanelReporteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listarTabla(String metodo) {
        totalCheque = 0;
        totalEfectivo = 0;
        totalTransferencia = 0;
        facturas = FXCollections.observableArrayList();
        facturaDao = new FacturaCabezeraJpaController();
        List<FacturaCabezera> facturasBD = facturaDao.findFacturaCabezeraEntitiesMetodoPago(metodo);

        for (FacturaCabezera fc : facturasBD) {
            facturas.add(fc);
            switch (fc.getMetodoPago()) {
                case "Efectivo": {
                    totalEfectivo += fc.getTotal();
                    break;
                }
                case "Transferencia": {
                    totalTransferencia += fc.getTotal();
                    break;
                }
                case "Cheque": {
                    totalCheque += fc.getTotal();
                    break;
                }
                default: {
                    break;
                }
            }
        }
        tablaReporte.setItems(facturas);
        lblEncontrados.setText(String.format("Encontrados(%d)", facturasBD.size()));
        lblEfectivo.setText(String.format("Efectivo: $ %.2f", totalEfectivo));
        lblCheque.setText(String.format("Cheque: $ %.2f", totalCheque));
        lbl1transferencia.setText(String.format("Transferencia: $ %.2f", totalTransferencia));

    }

}
