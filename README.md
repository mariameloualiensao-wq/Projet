# Gestion d'hôpital — Mini-projet JavaFX

Application de gestion d'hôpital développée en JavaFX, connectée à une base de données MySQL, dans le cadre du mini-projet **GI3 — Développement Java/IHM** (ENSAO).

## Description du projet

L'application permet de gérer deux entités principales : les **patients** et leurs **rendez-vous** médicaux. Elle propose un formulaire complet pour chaque entité, un tableau récapitulatif, une recherche/filtrage, des statistiques, et un export des données au format CSV.

## Fonctionnalités principales

- Gestion complète des patients (ajout, modification, suppression)
- Gestion complète des rendez-vous, liés à un patient et un médecin
- Recherche et filtrage des patients par nom/prénom
- Tableau de bord avec statistiques (totaux, taux de patients actifs, répartition des rendez-vous par statut)
- Export des patients au format CSV
- Personnalisation de la couleur d'interface (onglet Paramètres)
- Interface construite avec les contrôles JavaFX : TextField, TextArea, Button, Label, RadioButton, CheckBox, ComboBox, ListView, TableView, DatePicker, Slider, Spinner, ProgressBar, Tooltip, Menu/MenuBar, Alert, Accordion/TitledPane, ColorPicker, FileChooser

## Technologies utilisées

- Java 17
- JavaFX 17 (interface graphique)
- MySQL 8 (base de données)
- Maven (gestion du projet et des dépendances)
- Architecture MVC avec pattern DAO

## Prérequis

- Java JDK 17 ou supérieur installé
- MySQL Server installé et démarré
- Maven (intégré à IntelliJ, ou installé séparément)

## Installation

1. Cloner le dépôt :

  - git clone https://github.com/mariameloualiensao-wq/Projet.git

2. Créer la base de données en exécutant le script SQL fourni :

   - mysql -u root -p < sql/init_db.sql
  
3. Configurer les identifiants MySQL dans le fichier `src/main/java/com/hopital/dao/Database.java` (variables `USER` et `PASSWORD`) selon votre configuration locale.

4. Installer les dépendances Maven :

    -mvn clean install

## Lancement de l'application

    - mvn javafx:run
    
## Structure du projet

HopitalApp/

├── src/main/java/com/hopital/

│   ├── model/        → Classes Patient et RendezVous

│   ├── dao/           → Database, PatientDAO, RendezVousDAO

│   ├── controller/    → Contrôleurs liés aux vues FXML

│   └── MainApp.java   → Point d'entrée de l'application

├── src/main/resources/com/hopital/

│   ├── fxml/          → Vues de l'interface

│   └── css/           → Feuille de style

├── sql/

│   └── init_db.sql    → Script de création de la base de données

├── rapport/

│   └── Rapport_HopitalApp_GI3.pdf

└── README.md


## Auteurs

- Mariam Elouali
- Mahamat Sougouma Ali

Filière Génie Informatique — GI3, ENSAO, Université Mohammed Premier — Année 2025/2026

## Vidéo de démonstration

Lien Google Drive : [à compléter]
