package com.hopital.controller;

import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import com.hopital.dao.PatientDAO;
import com.hopital.model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class PatientController {

    // ===== Les champs du formulaire (liés via fx:id) =====
    @FXML private TextField champNom;
    @FXML private TextField champPrenom;
    @FXML private DatePicker champDateNaissance;
    @FXML private RadioButton radioHomme;
    @FXML private RadioButton radioFemme;
    @FXML private TextField champTelephone;
    @FXML private TextField champEmail;
    @FXML private ComboBox<String> comboGroupeSanguin;
    @FXML private CheckBox checkActif;
    @FXML private TextArea champAdresse;
    @FXML private TextField champRecherche;

    // ===== Les boutons =====
    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimer;

    // ===== Le tableau =====
    @FXML private TableView<Patient> tablePatients;
    @FXML private TableColumn<Patient, String> colNom;
    @FXML private TableColumn<Patient, String> colPrenom;
    @FXML private TableColumn<Patient, String> colTelephone;
    @FXML private TableColumn<Patient, String> colGroupe;
    @FXML private TableColumn<Patient, Boolean> colActif;

    // ===== Le DAO : notre "bibliothécaire" pour parler à MySQL =====
    private final PatientDAO patientDAO = new PatientDAO();

    // ===== La liste observable affichée dans le TableView =====
    private final ObservableList<Patient> listePatients = FXCollections.observableArrayList();

    // ===== Le patient actuellement sélectionné (pour Modifier/Supprimer) =====
    private Patient patientSelectionne = null;

    // ===== Méthode appelée automatiquement à l'ouverture de l'écran =====
    @FXML
    public void initialize() {
        // On regroupe les deux RadioButton pour qu'un seul soit sélectionné à la fois
        ToggleGroup groupeSexe = new ToggleGroup();
        radioHomme.setToggleGroup(groupeSexe);
        radioFemme.setToggleGroup(groupeSexe);

        // On remplit le ComboBox avec les groupes sanguins possibles
        comboGroupeSanguin.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");

        // ===== On dit à chaque colonne quelle donnée afficher =====
        // "PropertyValueFactory" va automatiquement appeler getNom(), getPrenom(), etc.
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colGroupe.setCellValueFactory(new PropertyValueFactory<>("groupeSanguin"));
        colActif.setCellValueFactory(new PropertyValueFactory<>("actif"));

        // On lie le TableView à notre liste observable
        tablePatients.setItems(listePatients);
        tablePatients.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Quand on clique sur une ligne du tableau, on remplit le formulaire
        tablePatients.getSelectionModel().selectedItemProperty().addListener((obs, ancien, nouveau) -> {
            if (nouveau != null) {
                remplirFormulaire(nouveau);
            }
        });

        // On charge les patients existants depuis MySQL au démarrage
        chargerPatients();
    }

    // ===== Recharge la liste des patients depuis la base de données =====
    private void chargerPatients() {
        listePatients.clear();
        List<Patient> patients = patientDAO.getAll();
        listePatients.addAll(patients);
    }

    // ===== Remplit le formulaire avec les infos d'un patient sélectionné =====
    private void remplirFormulaire(Patient p) {
        patientSelectionne = p;
        champNom.setText(p.getNom());
        champPrenom.setText(p.getPrenom());
        champDateNaissance.setValue(p.getDateNaissance());
        champTelephone.setText(p.getTelephone());
        champEmail.setText(p.getEmail());
        champAdresse.setText(p.getAdresse());
        comboGroupeSanguin.setValue(p.getGroupeSanguin());
        checkActif.setSelected(p.isActif());

        if ("Homme".equals(p.getSexe())) {
            radioHomme.setSelected(true);
        } else if ("Femme".equals(p.getSexe())) {
            radioFemme.setSelected(true);
        }
    }

    // ===== AJOUTER =====
    @FXML
    private void ajouterPatient() {
        if (champNom.getText().isEmpty() || champPrenom.getText().isEmpty()) {
            afficherAlerte("Erreur", "Le nom et le prénom sont obligatoires.", Alert.AlertType.ERROR);
            return;
        }

        Patient p = new Patient();
        remplirPatientDepuisFormulaire(p);

        patientDAO.ajouter(p);
        chargerPatients();
        effacerFormulaire();
        afficherAlerte("Succès", "Patient ajouté avec succès.", Alert.AlertType.INFORMATION);
    }

    // ===== MODIFIER =====
    @FXML
    private void modifierPatient() {
        if (patientSelectionne == null) {
            afficherAlerte("Erreur", "Sélectionnez d'abord un patient dans le tableau.", Alert.AlertType.WARNING);
            return;
        }

        remplirPatientDepuisFormulaire(patientSelectionne);
        patientDAO.modifier(patientSelectionne);
        chargerPatients();
        effacerFormulaire();
        afficherAlerte("Succès", "Patient modifié avec succès.", Alert.AlertType.INFORMATION);
    }

    // ===== SUPPRIMER =====
    @FXML
    private void supprimerPatient() {
        if (patientSelectionne == null) {
            afficherAlerte("Erreur", "Sélectionnez d'abord un patient dans le tableau.", Alert.AlertType.WARNING);
            return;
        }

        // On demande une confirmation avant de supprimer (bonne pratique !)
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Voulez-vous vraiment supprimer ce patient ?");

        confirmation.showAndWait().ifPresent(reponse -> {
            if (reponse == ButtonType.OK) {
                patientDAO.supprimer(patientSelectionne.getId());
                chargerPatients();
                effacerFormulaire();
            }
        });
    }

    // ===== EFFACER LE FORMULAIRE =====
    @FXML
    private void effacerFormulaire() {
        champNom.clear();
        champPrenom.clear();
        champDateNaissance.setValue(null);
        champTelephone.clear();
        champEmail.clear();
        champAdresse.clear();
        comboGroupeSanguin.setValue(null);
        checkActif.setSelected(false);
        radioHomme.setSelected(false);
        radioFemme.setSelected(false);
        patientSelectionne = null;
        tablePatients.getSelectionModel().clearSelection();
    }

    // ===== RECHERCHER (filtre simple sur nom/prénom) =====
    @FXML
    private void rechercherPatient() {
        String texte = champRecherche.getText().toLowerCase();

        if (texte.isEmpty()) {
            chargerPatients();
            return;
        }

        List<Patient> tous = patientDAO.getAll();
        listePatients.clear();

        for (Patient p : tous) {
            if (p.getNom().toLowerCase().contains(texte) || p.getPrenom().toLowerCase().contains(texte)) {
                listePatients.add(p);
            }
        }
    }

    // ===== Méthode utilitaire : remplit un objet Patient depuis le formulaire =====
    private void remplirPatientDepuisFormulaire(Patient p) {
        p.setNom(champNom.getText());
        p.setPrenom(champPrenom.getText());
        p.setDateNaissance(champDateNaissance.getValue());
        p.setTelephone(champTelephone.getText());
        p.setEmail(champEmail.getText());
        p.setAdresse(champAdresse.getText());
        p.setGroupeSanguin(comboGroupeSanguin.getValue());
        p.setActif(checkActif.isSelected());

        if (radioHomme.isSelected()) {
            p.setSexe("Homme");
        } else if (radioFemme.isSelected()) {
            p.setSexe("Femme");
        }
    }

    // ===== Méthode utilitaire : affiche une boîte de dialogue =====
    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alerte = new Alert(type);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(message);
        alerte.showAndWait();
    }
    // ===== EXPORT CSV =====
    @FXML
    private void exporterCSV() {

        // On ouvre une fenêtre pour choisir où sauvegarder le fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les patients en CSV");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Fichier CSV", "*.csv")
        );
        fileChooser.setInitialFileName("patients.csv");

        File fichier = fileChooser.showSaveDialog(tablePatients.getScene().getWindow());

        // Si l'utilisateur a annulé (fichier == null), on ne fait rien
        if (fichier == null) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fichier))) {

            // ===== Ligne d'en-tête =====
            writer.println("Nom;Prenom;DateNaissance;Sexe;Telephone;Email;Adresse;GroupeSanguin;Actif");

            // ===== Une ligne par patient =====
            for (Patient p : listePatients) {
                writer.println(
                        p.getNom() + ";" +
                                p.getPrenom() + ";" +
                                p.getDateNaissance() + ";" +
                                p.getSexe() + ";" +
                                p.getTelephone() + ";" +
                                p.getEmail() + ";" +
                                p.getAdresse() + ";" +
                                p.getGroupeSanguin() + ";" +
                                p.isActif()
                );
            }

            afficherAlerte("Succès", "Export réussi : " + fichier.getName(), Alert.AlertType.INFORMATION);

        } catch (IOException e) {
            afficherAlerte("Erreur", "Erreur lors de l'export : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}