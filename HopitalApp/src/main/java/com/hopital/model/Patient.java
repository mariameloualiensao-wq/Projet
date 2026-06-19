package com.hopital.model;

import java.time.LocalDate;

public class Patient {

    // ===== Attributs (correspondent aux colonnes de la table "patients") =====
    private int id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String sexe;
    private String telephone;
    private String email;
    private String adresse;
    private String groupeSanguin;
    private boolean actif;

    // ===== Constructeur vide (obligatoire pour certains usages) =====
    public Patient() {
    }

    // ===== Constructeur complet (sans id, car id est auto-généré par MySQL) =====
    public Patient(String nom, String prenom, LocalDate dateNaissance, String sexe,
                   String telephone, String email, String adresse,
                   String groupeSanguin, boolean actif) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
        this.groupeSanguin = groupeSanguin;
        this.actif = actif;
    }

    // ===== Getters et Setters =====
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getGroupeSanguin() {
        return groupeSanguin;
    }

    public void setGroupeSanguin(String groupeSanguin) {
        this.groupeSanguin = groupeSanguin;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    // ===== toString() : pratique pour afficher/déboguer un patient =====
    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}