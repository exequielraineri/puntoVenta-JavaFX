/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.raineri.puntoventa.Controller;

import com.raineri.puntoventa.Entity.Caja;
import com.raineri.puntoventa.Jpa.CajaJpaController;
import com.raineri.puntoventa.Jpa.exceptions.NonexistentEntityException;
import com.raineri.puntoventa.util.Alerta;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author ADMIN
 */
public class PanelCajaController implements Initializable {

    @FXML
    private TableView<Caja> tablaCaja;
    @FXML
    private TableColumn<Caja, Integer> col_id;
    @FXML
    private TableColumn<Caja, String> col_descripcion;
    @FXML
    private TableColumn<Caja, String> col_ingreso;
    @FXML
    private TableColumn<Caja, String> col_egreso;
    @FXML
    private TableColumn<Caja, String> col_fechaRegistro;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private RadioButton radioIngreso;
    @FXML
    private ToggleGroup tipo_caja;
    @FXML
    private RadioButton radioEgreso;
    @FXML
    private TextField txtImporte;
    @FXML
    private Label lblTotalIngreso;
    @FXML
    private Label lblTotalEgreso;
    @FXML
    private Label lblDiferencia;

    private CajaJpaController cajaDao;
    private Caja caja;

    private double totalIngreso = 0.0;
    private double totalEgreso = 0.0;

    DecimalFormat df = new DecimalFormat("###,##0.00");
    SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yy HH:mm");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_descripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        col_ingreso.setCellValueFactory((param) -> {
            if (param.getValue().getTipo().equals("I")) {
                //es ingreso
                return new SimpleObjectProperty<>("$" + df.format(param.getValue().getImporte()));
            } else {
                //es egreso
                return new SimpleObjectProperty<>("");
            }
        });
        col_egreso.setCellValueFactory((param) -> {
            if (param.getValue().getTipo().equals("E")) {
                //es egreso
                return new SimpleObjectProperty<>("$" + df.format(param.getValue().getImporte()));
            } else {
                //es ingreso
                return new SimpleObjectProperty<>("");
            }
        });
        col_fechaRegistro.setCellValueFactory((param) -> {
            return new SimpleObjectProperty<>(sf.format(param.getValue().getFechaRegistro()));
        });
        mostrarTablaCaja();
    }

    @FXML
    private void agregarACaja(ActionEvent event) {
        if (validarEntrada()) {
            cajaDao = new CajaJpaController();
            caja = new Caja();
            caja.setDescripcion(txtDescripcion.getText());
            caja.setImporte(Double.parseDouble(txtImporte.getText()));
            caja.setTipo(radioIngreso.isSelected() ? "I" : "E");
            caja.setFechaRegistro(new Date());
            cajaDao.create(caja);
            Alerta.mostrarAlertaInformacion("Ingreso exitoso!");
            txtDescripcion.setText(null);
            txtImporte.setText(null);
            mostrarTablaCaja();
        } else {
            Alerta.mostrarAlertaAdvertencia("Ingreso invalido.");
        }

    }

    private void mostrarTablaCaja() {
        totalEgreso = 0.0;
        totalIngreso = 0.0;
        tablaCaja.getItems().clear();
        cajaDao = new CajaJpaController();
        ObservableList detalleCaja = FXCollections.observableArrayList();
        List<Caja> listCaja = cajaDao.findCajaEntities();
        for (Caja c : listCaja) {
            if (c.getTipo().equals("I")) {
                totalIngreso += c.getImporte();
            } else if (c.getTipo().equals("E")) {
                totalEgreso += c.getImporte();
            }
            detalleCaja.add(c);
        }

        lblDiferencia.setText("$ " + df.format(totalIngreso - totalEgreso));
        lblTotalEgreso.setText("$" + df.format(totalEgreso));
        lblTotalIngreso.setText("$ " + df.format(totalIngreso));
        tablaCaja.setItems(detalleCaja);

    }

    private boolean validarEntrada() {
        if (txtDescripcion.getText().isEmpty() || txtDescripcion.getText().isBlank()) {
            return false;
        }

        if (txtImporte.getText().isEmpty() || txtImporte.getText().isBlank()) {
            return false;
        }

        if (radioEgreso.isSelected() == false && radioIngreso.isSelected() == false) {
            return false;
        }
        return true;
    }

    @FXML
    private void eliminarDetalleCaja(ActionEvent event) {
        if (tablaCaja.getSelectionModel().getSelectedItem() != null) {
            cajaDao = new CajaJpaController();
            int idCaja = tablaCaja.getSelectionModel().getSelectedItem().getId();
            if (Alerta.mostrarAlertaConfirmation("Segura desea eliminar?")) {
                try {
                    cajaDao.destroy(idCaja);
                    Alerta.mostrarAlertaInformacion("Se elimino exitosamente");
                    mostrarTablaCaja();
                } catch (NonexistentEntityException ex) {
                    Alerta.mostrarAlertaError(ex.getMessage());
                }
            }
        } else {
            Alerta.mostrarAlertaAdvertencia("Debe seleccionar un elemento");
        }

    }

}
