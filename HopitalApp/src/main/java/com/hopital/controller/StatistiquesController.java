package com.hopital.controller;

import com.hopital.dao.PatientDAO;
import com.hopital.dao.RendezVousDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class StatistiquesController {

    @FXML private Label labelTotalPatients;
    @FXML private Label labelTotalRdv;

    @FXML private ProgressBar progressActifs;
    @FXML private Label labelPourcentageActifs;

    @FXML private ProgressBar progressPlanifie;
    @FXML private Label labelPlanifie;
    @FXML private ProgressBar progressConfirme;
    @FXML private Label labelConfirme;
    @FXML private ProgressBar progressAnnule;
    @FXML private Label labelAnnule;

    private final PatientDAO patientDAO = new PatientDAO();
    private final RendezVousDAO rendezVousDAO = new RendezVousDAO();

    @FXML
    public void initialize() {
        actualiser();
    }

    @FXML
    private void actualiser() {

        // ===== Chiffres globaux =====
        int totalPatients = patientDAO.compterTous();
        int totalActifs = patientDAO.compterActifs();
        int totalRdv = rendezVousDAO.compterTous();

        labelTotalPatients.setText(String.valueOf(totalPatients));
        labelTotalRdv.setText(String.valueOf(totalRdv));

        // ===== Taux de patients actifs =====
        double tauxActifs = (totalPatients == 0) ? 0 : (double) totalActifs / totalPatients;
        progressActifs.setProgress(tauxActifs);
        labelPourcentageActifs.setText(Math.round(tauxActifs * 100) + " %");

        // ===== Répartition par statut =====
        int planifie = rendezVousDAO.compterParStatut("Planifié");
        int confirme = rendezVousDAO.compterParStatut("Confirmé");
        int annule = rendezVousDAO.compterParStatut("Annulé");

        labelPlanifie.setText(String.valueOf(planifie));
        labelConfirme.setText(String.valueOf(confirme));
        labelAnnule.setText(String.valueOf(annule));

        // On évite la division par zéro si aucun rendez-vous n'existe encore
        if (totalRdv > 0) {
            progressPlanifie.setProgress((double) planifie / totalRdv);
            progressConfirme.setProgress((double) confirme / totalRdv);
            progressAnnule.setProgress((double) annule / totalRdv);
        } else {
            progressPlanifie.setProgress(0);
            progressConfirme.setProgress(0);
            progressAnnule.setProgress(0);
        }
    }
}