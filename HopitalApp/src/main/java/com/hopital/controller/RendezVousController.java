package com.hopital.controller;

import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import com.hopital.dao.PatientDAO;
import com.hopital.dao.RendezVousDAO;
import com.hopital.model.Patient;
import com.hopital.model.RendezVous;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalTime;
import java.util.List;

public class RendezVousController {

    // ===== Champs du formulaire =====
    @FXML private ComboBox<Patient> comboPatient;
    @FXML private TextField champMedecin;
    @FXML private ComboBox<String> comboSpecialite;
    @FXML private DatePicker champDateRdv;
    @FXML private Spinner<Integer> spinnerHeure;
    @FXML private Spinner<Integer> spinnerMinute;
    @FXML private ComboBox<String> comboStatut;
    @FXML private Slider sliderPrix;
    @FXML private Label labelPrix;
    @FXML private TextArea champNotes;
    @FXML private ListView<String> listeMedecins;

    // ===== Tableau =====
    @FXML private TableView<RendezVous> tableRdv;
    @FXML private TableColumn<RendezVous, String> colPatient;
    @FXML private TableColumn<RendezVous, String> colMedecin;
    @FXML private TableColumn<RendezVous, String> colDateRdv;
    @FXML private TableColumn<RendezVous, String> colHeureRdv;
    @FXML private TableColumn<RendezVous, String> colStatut;
    @FXML private TableColumn<RendezVous, Double> colPrix;

    // ===== DAO =====
    private final RendezVousDAO rendezVousDAO = new RendezVousDAO();
    private final PatientDAO patientDAO = new PatientDAO();

    // ===== Données =====
    private final ObservableList<RendezVous> listeRdv = FXCollections.observableArrayList();
    private RendezVous rdvSelectionne = null;

    @FXML
    public void initialize() {

        // ===== Spinners pour l'heure et les minutes =====
        spinnerHeure.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9));
        spinnerMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));

        // ===== ComboBox Spécialité et Statut =====
        comboSpecialite.getItems().addAll("Cardiologie", "Pédiatrie", "Dermatologie", "Générale", "Orthopédie");
        comboStatut.getItems().addAll("Planifié", "Confirmé", "Annulé");

        // ===== ListView des médecins disponibles =====
        listeMedecins.getItems().addAll(
                "Dr. Bennani", "Dr. Alaoui", "Dr. Tazi", "Dr. Idrissi", "Dr. Chraibi"
        );
        System.out.println("Nombre de médecins dans la liste : " + listeMedecins.getItems().size());
        // ===== ComboBox Patient : on charge la liste des patients depuis la base =====
        comboPatient.getItems().addAll(patientDAO.getAll());

        // ===== Slider relié au Label (mise à jour en temps réel) =====
        sliderPrix.valueProperty().addListener((obs, ancien, nouveau) -> {
            labelPrix.setText(nouveau.intValue() + " DH");
        });

        // ===== Colonnes du tableau =====
        colPatient.setCellValueFactory(new PropertyValueFactory<>("nomPatient"));
        colMedecin.setCellValueFactory(new PropertyValueFactory<>("medecin"));
        colDateRdv.setCellValueFactory(new PropertyValueFactory<>("dateRdv"));
        colHeureRdv.setCellValueFactory(new PropertyValueFactory<>("heureRdv"));
        colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));

        tableRdv.setItems(listeRdv);
        tableRdv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ===== Sélection d'une ligne => remplir le formulaire =====
        tableRdv.getSelectionModel().selectedItemProperty().addListener((obs, ancien, nouveau) -> {
            if (nouveau != null) {
                remplirFormulaire(nouveau);
            }
        });

        chargerRendezVous();
    }

    private void chargerRendezVous() {
        listeRdv.clear();
        List<RendezVous> liste = rendezVousDAO.getAll();
        listeRdv.addAll(liste);
    }

    private void remplirFormulaire(RendezVous r) {
        rdvSelectionne = r;
        champMedecin.setText(r.getMedecin());
        comboSpecialite.setValue(r.getSpecialite());
        champDateRdv.setValue(r.getDateRdv());
        comboStatut.setValue(r.getStatut());
        champNotes.setText(r.getNotes());
        sliderPrix.setValue(r.getPrix());

        if (r.getHeureRdv() != null) {
            spinnerHeure.getValueFactory().setValue(r.getHeureRdv().getHour());
            spinnerMinute.getValueFactory().setValue(r.getHeureRdv().getMinute());
        }

        // Sélectionne le bon patient dans le ComboBox (recherche par id)
        for (Patient p : comboPatient.getItems()) {
            if (p.getId() == r.getPatientId()) {
                comboPatient.setValue(p);
                break;
            }
        }
    }

    @FXML
    private void ajouterRendezVous() {
        if (comboPatient.getValue() == null || champMedecin.getText().isEmpty()) {
            afficherAlerte("Erreur", "Le patient et le médecin sont obligatoires.", Alert.AlertType.ERROR);
            return;
        }

        RendezVous r = new RendezVous();
        remplirRdvDepuisFormulaire(r);

        rendezVousDAO.ajouter(r);
        chargerRendezVous();
        effacerFormulaire();
        afficherAlerte("Succès", "Rendez-vous ajouté avec succès.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void modifierRendezVous() {
        if (rdvSelectionne == null) {
            afficherAlerte("Erreur", "Sélectionnez d'abord un rendez-vous.", Alert.AlertType.WARNING);
            return;
        }

        remplirRdvDepuisFormulaire(rdvSelectionne);
        rendezVousDAO.modifier(rdvSelectionne);
        chargerRendezVous();
        effacerFormulaire();
        afficherAlerte("Succès", "Rendez-vous modifié avec succès.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void supprimerRendezVous() {
        if (rdvSelectionne == null) {
            afficherAlerte("Erreur", "Sélectionnez d'abord un rendez-vous.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer ce rendez-vous ?");

        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                rendezVousDAO.supprimer(rdvSelectionne.getId());
                chargerRendezVous();
                effacerFormulaire();
            }
        });
    }

    @FXML
    private void effacerFormulaire() {
        comboPatient.setValue(null);
        champMedecin.clear();
        comboSpecialite.setValue(null);
        champDateRdv.setValue(null);
        comboStatut.setValue(null);
        champNotes.clear();
        sliderPrix.setValue(200);
        spinnerHeure.getValueFactory().setValue(9);
        spinnerMinute.getValueFactory().setValue(0);
        rdvSelectionne = null;
        tableRdv.getSelectionModel().clearSelection();
    }

    private void remplirRdvDepuisFormulaire(RendezVous r) {
        r.setPatientId(comboPatient.getValue().getId());
        r.setMedecin(champMedecin.getText());
        r.setSpecialite(comboSpecialite.getValue());
        r.setDateRdv(champDateRdv.getValue());
        r.setHeureRdv(LocalTime.of(spinnerHeure.getValue(), spinnerMinute.getValue()));
        r.setStatut(comboStatut.getValue());
        r.setNotes(champNotes.getText());
        r.setPrix(sliderPrix.getValue());
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alerte = new Alert(type);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(message);
        alerte.showAndWait();
    }
    // ===== Sélection d'un médecin dans la ListView =====
    @FXML
    private void selectionnerMedecin(MouseEvent event) {
        String medecin = listeMedecins.getSelectionModel().getSelectedItem();
        if (medecin != null) {
            champMedecin.setText(medecin);
        }
    }
}