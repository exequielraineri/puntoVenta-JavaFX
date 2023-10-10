/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.raineri.puntoventa.Controller;

import com.raineri.puntoventa.Entity.Usuario;
import com.raineri.puntoventa.Jpa.UsuarioJpaController;
import com.raineri.puntoventa.util.Alerta;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author exera
 */
public class VistaLoginController implements Initializable {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnIniciar;
    @FXML
    private ImageView imagen;

    public static Usuario usuario = new Usuario();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    private void click(ActionEvent event) {

        UsuarioJpaController uDao = new UsuarioJpaController();
        List<Usuario> usuarios = uDao.findUsuario(txtUsuario.getText(), txtPassword.getText());
        if (usuarios.isEmpty()) {
            Alerta.mostrarAlertaAdvertencia("Usuario o Contraseña incorrecta");

        } else {

            try {
                usuario = usuarios.get(0);
                Parent root = FXMLLoader.load(getClass().getResource("../View/VistaPrincipal.fxml"));
                Scene scene = new Scene(root);

                Stage stage = new Stage();
                stage.setScene(scene);
                this.btnIniciar.getScene().getWindow().hide();
                stage.show();
            } catch (IOException ex) {
                Alerta.mostrarAlertaAdvertencia("Hubo un error: " + ex.getMessage());
                Logger.getLogger(VistaLoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
