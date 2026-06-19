package com.hopital.dao;

import com.hopital.model.Patient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    // ===== CREATE : ajouter un nouveau patient =====
    public void ajouter(Patient p) {
        String sql = "INSERT INTO patients (nom, prenom, date_naissance, sexe, telephone, email, adresse, groupe_sanguin, actif) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getPrenom());
            stmt.setDate(3, p.getDateNaissance() != null ? Date.valueOf(p.getDateNaissance()) : null);
            stmt.setString(4, p.getSexe());
            stmt.setString(5, p.getTelephone());
            stmt.setString(6, p.getEmail());
            stmt.setString(7, p.getAdresse());
            stmt.setString(8, p.getGroupeSanguin());
            stmt.setBoolean(9, p.isActif());

            stmt.executeUpdate();
            System.out.println("✅ Patient ajouté avec succès");

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    // ===== READ : récupérer tous les patients =====
    public List<Patient> getAll() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                patients.add(mapResultSetToPatient(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la lecture : " + e.getMessage());
        }

        return patients;
    }

    // ===== UPDATE : modifier un patient existant =====
    public void modifier(Patient p) {
        String sql = "UPDATE patients SET nom=?, prenom=?, date_naissance=?, sexe=?, telephone=?, " +
                "email=?, adresse=?, groupe_sanguin=?, actif=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNom());
            stmt.setString(2, p.getPrenom());
            stmt.setDate(3, p.getDateNaissance() != null ? Date.valueOf(p.getDateNaissance()) : null);
            stmt.setString(4, p.getSexe());
            stmt.setString(5, p.getTelephone());
            stmt.setString(6, p.getEmail());
            stmt.setString(7, p.getAdresse());
            stmt.setString(8, p.getGroupeSanguin());
            stmt.setBoolean(9, p.isActif());
            stmt.setInt(10, p.getId());

            stmt.executeUpdate();
            System.out.println("✅ Patient modifié avec succès");

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification : " + e.getMessage());
        }
    }

    // ===== DELETE : supprimer un patient =====
    public void supprimer(int id) {
        String sql = "DELETE FROM patients WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Patient supprimé avec succès");

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // ===== Méthode utilitaire : transforme une ligne SQL en objet Patient =====
    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setId(rs.getInt("id"));
        p.setNom(rs.getString("nom"));
        p.setPrenom(rs.getString("prenom"));

        Date date = rs.getDate("date_naissance");
        p.setDateNaissance(date != null ? date.toLocalDate() : null);

        p.setSexe(rs.getString("sexe"));
        p.setTelephone(rs.getString("telephone"));
        p.setEmail(rs.getString("email"));
        p.setAdresse(rs.getString("adresse"));
        p.setGroupeSanguin(rs.getString("groupe_sanguin"));
        p.setActif(rs.getBoolean("actif"));
        return p;
    }
    // ===== STATISTIQUES : nombre total de patients =====
    public int compterTous() {
        String sql = "SELECT COUNT(*) FROM patients";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du comptage : " + e.getMessage());
        }
        return 0;
    }

    // ===== STATISTIQUES : nombre de patients actifs =====
    public int compterActifs() {
        String sql = "SELECT COUNT(*) FROM patients WHERE actif = TRUE";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du comptage : " + e.getMessage());
        }
        return 0;
    }
}