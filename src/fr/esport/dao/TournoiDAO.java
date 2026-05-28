package fr.esport.dao;

import fr.esport.modele.Tournoi;
import fr.esport.util.ConnexionBDD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TournoiDAO {

    //  Lister tous les tournois
    public List<Tournoi> listerTournois() throws SQLException {
        List<Tournoi> liste = new ArrayList<>();
        String sql = "SELECT * FROM tournoi";
        try (Connection conn = ConnexionBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Tournoi t = new Tournoi();
                t.setIdTournoi(rs.getInt("id_tournoi"));
                t.setNom(rs.getString("nom"));
                t.setDateDebut(rs.getDate("date_debut"));
                t.setDateFin(rs.getDate("date_fin"));
                t.setType(rs.getString("type_format"));
                t.setCashPrize(rs.getDouble("dotation"));
                liste.add(t);
            }
        }
        return liste;
    }

    //  Inscription d'une équipe à un tournoi 
    public boolean inscrireEquipe(int idTournoi, int idEquipe) throws SQLException {
        Connection conn = ConnexionBDD.getConnection();
        // On crée une phase "Inscription" si elle n'existe pas pour ce tournoi
        String sqlPhase = "INSERT INTO phase (nom, id_tournoi) VALUES ('Phase Inscription', ?)";
        String sqlMatch = "INSERT INTO match_jeu (date_match, id_phase) VALUES (NOW(), ?)";
        String sqlManche = "INSERT INTO manche (nom_map, score_equipe1, score_equipe2, id_equipe1, id_equipe2, id_match) VALUES ('Map Attente', 0, 0, ?, ?, ?)";
        
        try {
            conn.setAutoCommit(false); // Mode transactionnel pour tout lier sans crash
            
            int idPhase;
            try (PreparedStatement pstmtP = conn.prepareStatement(sqlPhase, Statement.RETURN_GENERATED_KEYS)) {
                pstmtP.setInt(1, idTournoi);
                pstmtP.executeUpdate();
                try (ResultSet rs = pstmtP.getGeneratedKeys()) { rs.next(); idPhase = rs.getInt(1); }
            }
            
            int idMatch;
            try (PreparedStatement pstmtM = conn.prepareStatement(sqlMatch, Statement.RETURN_GENERATED_KEYS)) {
                pstmtM.setInt(1, idPhase);
                pstmtM.executeUpdate();
                try (ResultSet rs = pstmtM.getGeneratedKeys()) { rs.next(); idMatch = rs.getInt(1); }
            }
            
            try (PreparedStatement pstmtMa = conn.prepareStatement(sqlManche)) {
                pstmtMa.setInt(1, idEquipe);
                pstmtMa.setInt(2, idEquipe); // Initialisation de l'équipe face à elle-même pour l'arbre
                pstmtMa.setInt(3, idMatch);
                pstmtMa.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    //  Saisie du résultat d'un match 
    public boolean enregistrerScoreMatch(int idMatch, int scoreE1, int scoreE2) throws SQLException {
        String sql = "UPDATE manche SET score_equipe1 = ?, score_equipe2 = ? WHERE id_match = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, scoreE1);
            pstmt.setInt(2, scoreE2);
            pstmt.setInt(3, idMatch);
            return pstmt.executeUpdate() > 0;
        }
    }

    //  Saisie des statistiques individuelles d'un match 
    public boolean enregistrerStatsJoueur(int idJoueur, int idManche, int kills, int assists, int deaths, double performance) throws SQLException {
        String sql = "INSERT INTO jouer (id_joueur, id_manche, kills, assists, deaths, score_performance) VALUES (?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE kills = ?, assists = ?, deaths = ?, score_performance = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idJoueur);
            pstmt.setInt(2, idManche);
            pstmt.setInt(3, kills);
            pstmt.setInt(4, assists);
            pstmt.setInt(5, deaths);
            pstmt.setDouble(6, performance);
            
            // Pour le ON DUPLICATE KEY UPDATE
            pstmt.setInt(7, kills);
            pstmt.setInt(8, assists);
            pstmt.setInt(9, deaths);
            pstmt.setDouble(10, performance);
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    // Afficher l'arbre via ta VUE V3
    public void afficherClassementEtMatchs(int idTournoi) throws SQLException {
        String sql = "SELECT nom_phase, date_match, equipe_1, score_equipe1, score_equipe2, equipe_2 FROM vue_resultats_matchs WHERE id_tournoi = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idTournoi);
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n=== MATCHS ET PHASES DU TOURNOI ===");
                boolean dataFound = false;
                while (rs.next()) {
                    dataFound = true;
                    System.out.printf("[%s] %s VS %s (%d - %d)\n",
                        rs.getString("nom_phase"),
                        rs.getString("equipe_1"),
                        rs.getString("equipe_2"),
                        rs.getInt("score_equipe1"),
                        rs.getInt("score_equipe2")
                    );
                }
                if (!dataFound) System.out.println("Aucun match synchro trouvé pour ce tournoi.");
            }
        }
    }
}