-- Création de la base de données
CREATE DATABASE IF NOT EXISTS hopital_db;
USE hopital_db;

-- Table des patients
CREATE TABLE IF NOT EXISTS patients (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    date_naissance DATE,
    sexe VARCHAR(10),
    telephone VARCHAR(20),
    email VARCHAR(100),
    adresse TEXT,
    groupe_sanguin VARCHAR(5),
    actif BOOLEAN DEFAULT TRUE
    );

-- Table des rendez-vous
CREATE TABLE IF NOT EXISTS rendez_vous (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           patient_id INT NOT NULL,
                                           medecin VARCHAR(100),
    specialite VARCHAR(100),
    date_rdv DATE,
    heure_rdv TIME,
    statut VARCHAR(20) DEFAULT 'Planifié',
    notes TEXT,
    prix DECIMAL(10,2),
    FOREIGN KEY (patient_id) REFERENCES patients(id) ON DELETE CASCADE
    );