/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.raineri.puntoventa.Controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author exera
 */
public class VistaPrincipalController implements Initializable {
    
    @FXML
    private BorderPane bp;
    @FXML
    private Label lblUser;
    @FXML
    private Button btnHome;
    @FXML
    private Button btnInventario;
    @FXML
    private Button btnVenta;
    @FXML
    private Button btnReporte;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Label txtFecha;
    @FXML
    private Button btnReporte1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            lblUser.setText(VistaLoginController.usuario.getApellido() + ", " + VistaLoginController.usuario.getNombre());
            txtFecha.setText(sf.format(new Date()));
            Parent root = FXMLLoader.load(getClass().getResource("../View/panelHome.fxml"));
            bp.setCenter(root);
        } catch (IOException ex) {
            Logger.getLogger(VistaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void actionHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/panelHome.fxml"));
            bp.setCenter(root);
        } catch (IOException ex) {
            Logger.getLogger(VistaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void actionInventario(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/panelInventario.fxml"));
            bp.setCenter(root);
        } catch (IOException ex) {
            Logger.getLogger(VistaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void actionVenta(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/panelVenta.fxml"));
            bp.setCenter(root);
        } catch (IOException ex) {
            Logger.getLogger(VistaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void actionReporte(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/panelReporte.fxml"));
            bp.setCenter(root);
        } catch (IOException ex) {
            Logger.getLogger(VistaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void cerrarSesion(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("Cierre de Sesion");
        alert.setContentText("Desea cerrar la sesión?");
        
        ButtonType botonSi = new ButtonType("Si");
        ButtonType botonNo = new ButtonType("No");
        alert.getButtonTypes().setAll(botonSi, botonNo);
        
        Optional<ButtonType> showAndWait = alert.showAndWait();
        if (showAndWait.get() == botonSi) {
            
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../View/VistaLogin.fxml"));
                Scene scene = new Scene(root);
                this.btnHome.getScene().getWindow().hide();
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(VistaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        alert.close();
    }

    @FXML
    private void actionProveedores(ActionEvent event) {
         try {
            Parent root = FXMLLoader.load(getClass().getResource("../View/panelProveedores.fxml"));
            bp.setCenter(root);
        } catch (IOException ex) {
            Logger.getLogger(VistaPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
