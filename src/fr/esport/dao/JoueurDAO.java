package fr.esport.dao;

import fr.esport.modele.Joueur;
import fr.esport.util.ConnexionBDD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JoueurDAO {

    // Ajouter un nouveau joueur
    public boolean ajouterJoueur(Joueur j) throws SQLException {
        String sql = "INSERT INTO joueur (pseudo, nom, prenom, date_naissance, nationalite, niveau) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnexionBDD.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, j.getPseudo());
            pstmt.setString(2, j.getNom());
            pstmt.setString(3, j.getPrenom());
            pstmt.setDate(4, j.getDateNaissance());
            pstmt.setString(5, j.getNationalite());
            pstmt.setString(6, j.getNiveau());
            return pstmt.executeUpdate() > 0;
        }
    }

    // Lister tous les joueurs
    public List<Joueur> listerJoueurs() throws SQLException {
        List<Joueur> liste = new ArrayList<>();
        String sql = "SELECT * FROM joueur";
        try (Connection connection = ConnexionBDD.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                liste.add(new Joueur(
                    rs.getInt("id_joueur"), rs.getString("pseudo"),
                    rs.getString("nom"), rs.getString("prenom"),
                    rs.getDate("date_naissance"), rs.getString("nationalite"),
                    rs.getString("niveau")
                ));
            }
        }
        return liste;
    }

    // Rechercher un joueur par pseudo
    public Joueur rechercherParPseudo(String pseudo) throws SQLException {
        String sql = "SELECT * FROM joueur WHERE pseudo = ?";
        try (Connection connection = ConnexionBDD.getConnection(); 
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, pseudo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Joueur(
                        rs.getInt("id_joueur"), rs.getString("pseudo"),
                        rs.getString("nom"), rs.getString("prenom"),
                        rs.getDate("date_naissance"), rs.getString("nationalite"),
                        rs.getString("niveau")
                    );
                }
            }
        }
        return null;
    }

    // Modifier les informations d'un joueur
    public boolean modifierJoueur(Joueur j) throws SQLException {
        String sql = "UPDATE joueur SET pseudo = ?, nom = ?, prenom = ?, date_naissance = ?, nationalite = ?, niveau = ? WHERE id_joueur = ?";
        try (Connection connection = ConnexionBDD.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            
            pstmt.setString(1, j.getPseudo());
            pstmt.setString(2, j.getNom());
            pstmt.setString(3, j.getPrenom());
            pstmt.setDate(4, j.getDateNaissance());
            pstmt.setString(5, j.getNationalite());
            pstmt.setString(6, j.getNiveau());
            pstmt.setInt(7, j.getIdJoueur());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    // Supprimer un joueur
    public boolean supprimerJoueur(int idJoueur) throws SQLException {
        String sql = "DELETE FROM joueur WHERE id_joueur = ?";
        try (Connection connection = ConnexionBDD.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idJoueur);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Utilisation de la vue SQL Stats globales
    public void afficherProfilStatistiques(String pseudo) {
        String sql = "SELECT * FROM vue_general_stats_joueurs WHERE pseudo = ?";
        try (Connection connection = ConnexionBDD.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, pseudo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\n=== STATISTIQUES GLOBALES DE " + rs.getString("pseudo") + " ===");
                    System.out.println("Nationalité : " + rs.getString("nationalite"));
                    System.out.println("Matchs joués : " + rs.getInt("matchs_joues"));
                    System.out.println("Total Kills  : " + rs.getInt("total_kills"));
                    System.out.println("Total Deaths : " + rs.getInt("total_deaths"));
                    System.out.println("KDA Global   : " + rs.getDouble("kda_global"));
                    System.out.println("===========================");
                } else {
                    System.out.println("Aucune statistique disponible.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur d'affichage : " + e.getMessage());
        }
    }
}