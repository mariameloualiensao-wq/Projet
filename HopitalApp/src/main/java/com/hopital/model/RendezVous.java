package com.hopital.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class RendezVous {

    // ===== Attributs (correspondent aux colonnes de la table "rendez_vous") =====
    private int id;
    private int patientId;       // lien vers le Patient (clé étrangère)
    private String medecin;
    private String specialite;
    private LocalDate dateRdv;
    private LocalTime heureRdv;
    private String statut;
    private String notes;
    private double prix;

    // Champ "bonus" pratique : pour afficher le nom du patient directement
    // dans le tableau, sans devoir refaire une recherche à chaque fois.
    private String nomPatient;

    // ===== Constructeur vide =====
    public RendezVous() {
    }

    // ===== Constructeur complet (sans id, auto-généré par MySQL) =====
    public RendezVous(int patientId, String medecin, String specialite,
                      LocalDate dateRdv, LocalTime heureRdv, String statut,
                      String notes, double prix) {
        this.patientId = patientId;
        this.medecin = medecin;
        this.specialite = specialite;
        this.dateRdv = dateRdv;
        this.heureRdv = heureRdv;
        this.statut = statut;
        this.notes = notes;
        this.prix = prix;
    }

    // ===== Getters et Setters =====
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getMedecin() {
        return medecin;
    }

    public void setMedecin(String medecin) {
        this.medecin = medecin;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public LocalDate getDateRdv() {
        return dateRdv;
    }

    public void setDateRdv(LocalDate dateRdv) {
        this.dateRdv = dateRdv;
    }

    public LocalTime getHeureRdv() {
        return heureRdv;
    }

    public void setHeureRdv(LocalTime heureRdv) {
        this.heureRdv = heureRdv;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getNomPatient() {
        return nomPatient;
    }

    public void setNomPatient(String nomPatient) {
        this.nomPatient = nomPatient;
    }

    @Override
    public String toString() {
        return medecin + " - " + dateRdv + " " + heureRdv;
    }
}
