package com.hopital;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/hopital/fxml/Main.fxml"));
        Scene scene = new Scene(root, 1100, 700);

        // ===== Chargement de la feuille de style CSS =====
        scene.getStylesheets().add(getClass().getResource("/com/hopital/css/style.css").toExternalForm());

        stage.setTitle("Gestion d'hôpital");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}