package com.hopital.controller;

import javafx.application.Platform;

public class MainController {

    // Méthode appelée quand on clique sur "Quitter" dans le menu
    public void quitter() {
        Platform.exit();
    }
}