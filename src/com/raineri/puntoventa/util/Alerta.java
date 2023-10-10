/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raineri.puntoventa.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
}
