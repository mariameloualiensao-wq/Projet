package com.hopital.controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class ParametresController {

    @FXML private ColorPicker colorPicker;

    // ===== Méthode appelée automatiquement à l'ouverture de l'écran =====
    @FXML
    public void initialize() {
        colorPicker.setValue(Color.web("#3b82f6"));
    }

    // ===== Applique la couleur choisie à toute la fenêtre =====
    @FXML
    private void appliquerCouleur() {
        Color couleur = colorPicker.getValue();
        String couleurHex = toHex(couleur);

        Parent racine = colorPicker.getScene().getRoot();
        racine.setStyle("-fx-accent: " + couleurHex + ";");
    }

    // ===== Remet la couleur par défaut =====
    @FXML
    private void reinitialiserCouleur() {
        Parent racine = colorPicker.getScene().getRoot();
        racine.setStyle("");
        colorPicker.setValue(Color.web("#3b82f6"));
    }

    // ===== Convertit une Color JavaFX en code hexadécimal utilisable en CSS =====
    private String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}