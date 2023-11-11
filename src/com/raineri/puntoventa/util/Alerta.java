/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raineri.puntoventa.util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alerta {

    public static void mostrarAlertaInformacion(String mensaje) {
        mostrarAlerta(AlertType.INFORMATION, "Información", mensaje);
    }

    public static void mostrarAlertaAdvertencia(String mensaje) {
        mostrarAlerta(AlertType.WARNING, "Advertencia", mensaje);
    }

    public static void mostrarAlertaError(String mensaje) {
        mostrarAlerta(AlertType.ERROR, "Error", mensaje);
    }

    public static void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static boolean mostrarAlertaConfirmation(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(mensaje);
        ButtonType btnsi = new ButtonType("Si");
        ButtonType btnno = new ButtonType("No");
        alert.getButtonTypes().setAll(btnsi, btnno);
        Optional<ButtonType> btn = alert.showAndWait();
        if (btn.get() == btnsi) {
            return true;
        } else {
            return false;
        }

    }

}
