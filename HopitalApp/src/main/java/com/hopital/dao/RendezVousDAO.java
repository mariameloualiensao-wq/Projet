package com.hopital.dao;

import com.hopital.model.RendezVous;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RendezVousDAO {

    // ===== CREATE : ajouter un nouveau rendez-vous =====
    public void ajouter(RendezVous r) {
        String sql = "INSERT INTO rendez_vous (patient_id, medecin, specialite, date_rdv, heure_rdv, statut, notes, prix) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, r.getPatientId());
            stmt.setString(2, r.getMedecin());
            stmt.setString(3, r.getSpecialite());
            stmt.setDate(4, r.getDateRdv() != null ? Date.valueOf(r.getDateRdv()) : null);
            stmt.setTime(5, r.getHeureRdv() != null ? Time.valueOf(r.getHeureRdv()) : null);
            stmt.setString(6, r.getStatut());
            stmt.setString(7, r.getNotes());
            stmt.setDouble(8, r.getPrix());

            stmt.executeUpdate();
            System.out.println("✅ Rendez-vous ajouté avec succès");

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    // ===== READ : récupérer tous les rendez-vous (avec le nom du patient lié) =====
    public List<RendezVous> getAll() {
        List<RendezVous> liste = new ArrayList<>();

        // On utilise un JOIN pour récupérer en même temps le nom du patient
        String sql = "SELECT rv.*, p.nom, p.prenom FROM rendez_vous rv " +
                "JOIN patients p ON rv.patient_id = p.id " +
                "ORDER BY rv.date_rdv DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                liste.add(mapResultSetToRendezVous(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la lecture : " + e.getMessage());
        }

        return liste;
    }

    // ===== UPDATE : modifier un rendez-vous existant =====
    public void modifier(RendezVous r) {
        String sql = "UPDATE rendez_vous SET patient_id=?, medecin=?, specialite=?, date_rdv=?, " +
                "heure_rdv=?, statut=?, notes=?, prix=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, r.getPatientId());
            stmt.setString(2, r.getMedecin());
            stmt.setString(3, r.getSpecialite());
            stmt.setDate(4, r.getDateRdv() != null ? Date.valueOf(r.getDateRdv()) : null);
            stmt.setTime(5, r.getHeureRdv() != null ? Time.valueOf(r.getHeureRdv()) : null);
            stmt.setString(6, r.getStatut());
            stmt.setString(7, r.getNotes());
            stmt.setDouble(8, r.getPrix());
            stmt.setInt(9, r.getId());

            stmt.executeUpdate();
            System.out.println("✅ Rendez-vous modifié avec succès");

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification : " + e.getMessage());
        }
    }

    // ===== DELETE : supprimer un rendez-vous =====
    public void supprimer(int id) {
        String sql = "DELETE FROM rendez_vous WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Rendez-vous supprimé avec succès");

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // ===== Méthode utilitaire : transforme une ligne SQL en objet RendezVous =====
    private RendezVous mapResultSetToRendezVous(ResultSet rs) throws SQLException {
        RendezVous r = new RendezVous();
        r.setId(rs.getInt("id"));
        r.setPatientId(rs.getInt("patient_id"));
        r.setMedecin(rs.getString("medecin"));
        r.setSpecialite(rs.getString("specialite"));

        Date date = rs.getDate("date_rdv");
        r.setDateRdv(date != null ? date.toLocalDate() : null);

        Time time = rs.getTime("heure_rdv");
        r.setHeureRdv(time != null ? time.toLocalTime() : null);

        r.setStatut(rs.getString("statut"));
        r.setNotes(rs.getString("notes"));
        r.setPrix(rs.getDouble("prix"));

        // Champ bonus : nom complet du patient (grâce au JOIN)
        r.setNomPatient(rs.getString("prenom") + " " + rs.getString("nom"));

        return r;
    }
    // ===== STATISTIQUES : nombre total de rendez-vous =====
    public int compterTous() {
        String sql = "SELECT COUNT(*) FROM rendez_vous";
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

    // ===== STATISTIQUES : nombre de rendez-vous par statut =====
    public int compterParStatut(String statut) {
        String sql = "SELECT COUNT(*) FROM rendez_vous WHERE statut = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, statut);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du comptage : " + e.getMessage());
        }
        return 0;
    }
}