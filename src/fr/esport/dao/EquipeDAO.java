package fr.esport.dao;

import fr.esport.modele.Equipe;
import fr.esport.util.ConnexionBDD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeDAO {

    // Ajouter une équipe (DML aligné)
    public boolean ajouterEquipe(Equipe e) throws SQLException {
        String sql = "INSERT INTO equipe (nom, logo, date_creation, pays) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, e.getNom());
            pstmt.setString(2, e.getLogo());
            pstmt.setDate(3, e.getDateCreation());
            pstmt.setString(4, e.getPays());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Lister les équipes (Règle l'erreur 'structure' Column not found !)
    public List<Equipe> listerEquipes() throws SQLException {
        List<Equipe> liste = new ArrayList<>();
        String sql = "SELECT * FROM equipe";
        try (Connection conn = ConnexionBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Equipe(
                    rs.getInt("id_equipe"), 
                    rs.getString("nom"),
                    rs.getString("logo"),
                    rs.getDate("date_creation"),
                    rs.getString("pays")
                ));
            }
        }
        return liste;
    }

    // Rechercher une équipe par son nom exact
    public Equipe rechercherParNom(String nom) throws SQLException {
        String sql = "SELECT * FROM equipe WHERE nom = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Equipe(
                        rs.getInt("id_equipe"), 
                        rs.getString("nom"),
                        rs.getString("logo"),
                        rs.getDate("date_creation"),
                        rs.getString("pays")
                    );
                }
            }
        }
        return null;
    }

    // Modifier TOUTES les informations d'une équipe
    public boolean modifierEquipe(Equipe e) throws SQLException {
        String sql = "UPDATE equipe SET nom = ?, logo = ?, date_creation = ?, pays = ? WHERE id_equipe = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, e.getNom());
            pstmt.setString(2, e.getLogo());
            pstmt.setDate(3, e.getDateCreation());
            pstmt.setString(4, e.getPays());
            pstmt.setInt(5, e.getIdEquipe());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Gérer le Roster (Aligné sur ta table 'roster' associative ternaire)
    public boolean ajouterJoueurAuRoster(int idEquipe, int idJoueur, int idJeu, String role) throws SQLException {
        String sql = "INSERT INTO roster (id_equipe, id_joueur, id_jeu, date_debut, role_joueur) VALUES (?, ?, ?, NOW(), ?) " +
                     "ON DUPLICATE KEY UPDATE role_joueur = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEquipe);
            pstmt.setInt(2, idJoueur);
            pstmt.setInt(3, idJeu);
            pstmt.setString(4, role);
            pstmt.setString(5, role);
            return pstmt.executeUpdate() > 0;
        }
    }

    // ALIGNÉ SUR TA VUE V2 : Afficher le roster actif d'une équipe
    public void afficherMembresEquipe(String nomEquipe) throws SQLException {
        String sql = "SELECT pseudo_joueur, nom_jeu, role_joueur FROM vue_rosters_actifs WHERE nom_equipe = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomEquipe);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n=== COMPOSITION DE L'ÉQUIPE : " + nomEquipe + " ===");
                boolean tracking = false;
                while (rs.next()) {
                    tracking = true;
                    System.out.println("-> " + rs.getString("pseudo_joueur") + " | Jeu : " + rs.getString("nom_jeu") + " | Rôle : " + rs.getString("role_joueur"));
                }
                if (!tracking) System.out.println("Aucun roster actif trouvé pour cette équipe.");
                System.out.println("==========================================");
            }
        }
    }
}