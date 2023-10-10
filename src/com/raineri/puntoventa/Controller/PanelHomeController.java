/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.raineri.puntoventa.Controller;

import com.raineri.puntoventa.Entity.FacturaCabezera;
import com.raineri.puntoventa.Jpa.FacturaCabezeraJpaController;
import com.raineri.puntoventa.Jpa.ProductoJpaController;
import com.raineri.puntoventa.Jpa.ProveedorJpaController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author exera
 */
public class PanelHomeController implements Initializable {

    @FXML
    private Label lblCantProductos;
    @FXML
    private Label lblCantVentas;
    @FXML
    private Label lblCantProveedores;

    ProveedorJpaController proveedorDao;
    ProductoJpaController productoDao;
    FacturaCabezeraJpaController ventaDao;
    @FXML
    private LineChart lineChart;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        proveedorDao = new ProveedorJpaController();
        productoDao = new ProductoJpaController();
        ventaDao = new FacturaCabezeraJpaController();
        lblCantProductos.setText(String.valueOf(productoDao.getProductoCount()));
        lblCantProveedores.setText(String.valueOf(proveedorDao.getProveedorCount()));
        lblCantVentas.setText(String.valueOf(ventaDao.getFacturaCabezeraCount()));

        cargarAreaChart();

    }

    private void cargarAreaChart() {
        XYChart.Series series = new XYChart.Series();
        List<FacturaCabezera> ventasRealizadas = ventaDao.findFacturaCabezeraEntities();

        series.setName("Importe total");
        XYChart.Data<String, Double> enero = new XYChart.Data<>("Enero", 0.0);
        XYChart.Data<String, Double> febrero = new XYChart.Data<>("Febrero", 0.0);
        XYChart.Data<String, Double> marzo = new XYChart.Data<>("Marzo", 0.0);
        XYChart.Data<String, Double> abril = new XYChart.Data<>("Abril", 0.0);
        XYChart.Data<String, Double> mayo = new XYChart.Data<>("Mayo", 0.0);
        XYChart.Data<String, Double> junio = new XYChart.Data<>("Junio", 0.0);
        XYChart.Data<String, Double> julio = new XYChart.Data<>("Julio", 0.0);
        XYChart.Data<String, Double> agosto = new XYChart.Data<>("Agosto", 0.0);
        XYChart.Data<String, Double> septiembre = new XYChart.Data<>("Septiembre", 0.0);
        XYChart.Data<String, Double> octubre = new XYChart.Data<>("Octubre", 0.0);
        XYChart.Data<String, Double> noviembre = new XYChart.Data<>("Noviembre", 0.0);
        XYChart.Data<String, Double> diciembre = new XYChart.Data<>("Diciembre", 0.0);

        for (FacturaCabezera vr : ventasRealizadas) {
            switch (vr.getFechaEmision().getMonth()) {
                case 0 ->
                    enero.YValueProperty().setValue(+vr.getTotal());
                case 1 ->
                    febrero.YValueProperty().setValue(+vr.getTotal());
                case 2 ->
                    marzo.YValueProperty().setValue(+vr.getTotal());
                case 3 ->
                    abril.YValueProperty().setValue(+vr.getTotal());
                case 4 ->
                    mayo.YValueProperty().setValue(+vr.getTotal());
                case 5 ->
                    junio.YValueProperty().setValue(+vr.getTotal());
                case 6 ->
                    julio.YValueProperty().setValue(+vr.getTotal());
                case 7 ->
                    agosto.YValueProperty().setValue(+vr.getTotal());
                case 8 ->
                    septiembre.YValueProperty().setValue(+vr.getTotal());
                case 9 ->
                    octubre.YValueProperty().setValue(+vr.getTotal());
                case 10 ->
                    noviembre.YValueProperty().setValue(+vr.getTotal());
                case 11 ->
                    diciembre.YValueProperty().setValue(+vr.getTotal());
                default -> {
                    break;
                }
            }

        }

        series.getData().addAll(enero, febrero, marzo, abril, mayo, junio, julio, agosto, septiembre, octubre, noviembre, diciembre);
        lineChart.getData().add(series);
        
    }

}
