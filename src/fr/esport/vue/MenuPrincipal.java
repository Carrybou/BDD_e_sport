package fr.esport.vue;

import fr.esport.dao.EquipeDAO;
import fr.esport.dao.JoueurDAO;
import fr.esport.dao.TournoiDAO;
import fr.esport.modele.Joueur;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;

public class MenuPrincipal {
    private static final Scanner scanner = new Scanner(System.in);
    private static final JoueurDAO joueurDAO = new JoueurDAO();
    private static final EquipeDAO equipeDAO = new EquipeDAO();
private static final TournoiDAO tournoiDAO = new TournoiDAO();

    public static void main(String[] args) {
        int choix = 0;
        do {
            System.out.println("\n--- GESTIONNAIRE PLATFORME E-SPORT ---");
            System.out.println("1. Gestion des joueurs");
            System.out.println("2. Gestion des équipes");
            System.out.println("3. Gestion des tournois");
            System.out.println("4. Saisir le résultat d'un match");
            System.out.println("5. Classement d'un tournoi");
            System.out.println("6. Statistiques d'un joueur");
            System.out.println("7. Quitter");
            System.out.print("Votre choix : ");
            
            try { choix = Integer.parseInt(scanner.nextLine()); } catch (NumberFormatException e) { choix = 0; }

            try {
                switch (choix) {
                    case 1: menuJoueurs(); break;
                    case 2: menuEquipes(); break;
                    case 3: menuTournois(); break;
                    case 4: saisirResultatMatch(); break;
                    case 5: afficherClassementTournoi(); break;
                    case 6: consulterStatsJoueur(); break;
                    case 7: System.out.println("Fermeture de l'application. À bientôt !"); break;
                    default: System.out.println("Choix invalide.");
                }
            } catch (SQLException e) {
                System.err.println("Erreur SQL : " + e.getMessage());
            }
        } while (choix != 7);
    }

    private static void menuJoueurs() throws SQLException {
        int choix = 0;
        System.out.println("\n--- GESTION DES JOUEURS ---");
        System.out.println("1. Ajouter un nouveau joueur");
        System.out.println("2. Lister tous les joueurs");
        System.out.println("3. Rechercher un joueur par pseudo");
        System.out.println("4. Modifier les informations d'un joueur");
        System.out.println("5. Supprimer un joueur");
        System.out.print("Choix : ");
        try { choix = Integer.parseInt(scanner.nextLine()); } catch (NumberFormatException e) { return; }

        switch (choix) {
            case 1:
                Joueur j = new Joueur();
                System.out.print("Pseudo : "); j.setPseudo(scanner.nextLine());
                System.out.print("Nom : "); j.setNom(scanner.nextLine());
                System.out.print("Prénom : "); j.setPrenom(scanner.nextLine());
                System.out.print("Date Naissance (AAAA-MM-JJ) : "); j.setDateNaissance(Date.valueOf(scanner.nextLine()));
                System.out.print("Nationalité : "); j.setNationalite(scanner.nextLine());
                System.out.print("Niveau : "); j.setNiveau(scanner.nextLine());
                if (joueurDAO.ajouterJoueur(j)) System.out.println("Joueur ajouté !");
                break;
            case 2:
                System.out.println("\nListe des joueurs enregistrés :");
                for (Joueur joueur : joueurDAO.listerJoueurs()) {
                    System.out.println("- " + joueur.getPseudo() + " (" + joueur.getNationalite() + ")");
                }
                break;
            case 3:
                System.out.print("Pseudo recherché : ");
                String pseudoRecherche = scanner.nextLine();
                Joueur jt = joueurDAO.rechercherParPseudo(pseudoRecherche);
                
                if (jt != null) {
                    System.out.println("\n========================================");
                    System.out.println("          FICHE JOUEUR TROUVÉ           ");
                    System.out.println("========================================");
                    System.out.println("ID Unique        : " + jt.getIdJoueur());
                    System.out.println("Pseudo           : " + jt.getPseudo());
                    System.out.println("Nom complet      : " + jt.getPrenom() + " " + jt.getNom());
                    System.out.println("Date de Naissance: " + jt.getDateNaissance());
                    System.out.println("Nationalité      : " + jt.getNationalite());
                    System.out.println("Niveau / Rang    : " + jt.getNiveau());
                    System.out.println("========================================");
                } else {
                    System.out.println("\n[Erreur] Aucun joueur trouvé avec le pseudo : " + pseudoRecherche);
                }
                break;
            case 4:
                System.out.print("Pseudo du joueur à modifier : ");
                String pseudoModif = scanner.nextLine();
                Joueur jm = joueurDAO.rechercherParPseudo(pseudoModif);
                
                if (jm != null) {
                    System.out.println("\n--- MODIFICATION DU JOUEUR (Laissez vide pour ne pas changer) ---");
                    
                    System.out.print("Nouveau Pseudo (" + jm.getPseudo() + ") : ");
                    String nvPseudo = scanner.nextLine();
                    if (!nvPseudo.trim().isEmpty()) jm.setPseudo(nvPseudo);
                    
                    System.out.print("Nouveau Nom (" + jm.getNom() + ") : ");
                    String nvNom = scanner.nextLine();
                    if (!nvNom.trim().isEmpty()) jm.setNom(nvNom);
                    
                    System.out.print("Nouveau Prénom (" + jm.getPrenom() + ") : ");
                    String nvPrenom = scanner.nextLine();
                    if (!nvPrenom.trim().isEmpty()) jm.setPrenom(nvPrenom);
                    
                    System.out.print("Nouvelle Date de Naissance (" + jm.getDateNaissance() + " | AAAA-MM-JJ) : ");
                    String nvDate = scanner.nextLine();
                    if (!nvDate.trim().isEmpty()) jm.setDateNaissance(Date.valueOf(nvDate));
                    
                    System.out.print("Nouvelle Nationalité (" + jm.getNationalite() + ") : ");
                    String nvNat = scanner.nextLine();
                    if (!nvNat.trim().isEmpty()) jm.setNationalite(nvNat);
                    
                    System.out.print("Nouveau Niveau / Rang (" + jm.getNiveau() + ") : ");
                    String nvNiveau = scanner.nextLine();
                    if (!nvNiveau.trim().isEmpty()) jm.setNiveau(nvNiveau);
                    
                    // Envoi des modifications au DAO
                    if (joueurDAO.modifierJoueur(jm)) {
                        System.out.println("\n[Succès] Les informations du joueur ont été mises à jour !");
                    } else {
                        System.out.println("\n[Erreur] Impossible de mettre à jour le joueur en base.");
                    }
                } else {
                    System.out.println("\n[Erreur] Aucun joueur trouvé avec le pseudo : " + pseudoModif);
                }
                break;
            case 5:
                System.out.print("Pseudo du joueur à supprimer : ");
                Joueur js = joueurDAO.rechercherParPseudo(scanner.nextLine());
                if (js != null && joueurDAO.supprimerJoueur(js.getIdJoueur())) System.out.println("Joueur supprimé.");
                break;
        }
    }

    private static void menuEquipes() throws SQLException {
    System.out.println("\n--- GESTION DES ÉQUIPES ---");
    System.out.println("1. Lister les équipes");
    System.out.println("2. Consulter la composition (Roster) d'une équipe");
    System.out.println("3. Modifier les informations d'une équipe");
    System.out.println("4. Gérer le Roster (Ajouter/Transférer un joueur)");
    System.out.print("Votre choix : ");
    int choix = Integer.parseInt(scanner.nextLine());
    
    switch (choix) {
        case 1:
            System.out.println("\nListe des équipes en base :");
            for (fr.esport.modele.Equipe e : equipeDAO.listerEquipes()) {
                System.out.println("- " + e.getNom() + " [Pays : " + e.getPays() + " | Logo : " + e.getLogo() + "]");
            }
            break;
            
        case 2:
            System.out.print("Entrez le nom exact de l'équipe (ex: T1, G2 Esports, Karmine Corp) : ");
            String nomConsult = scanner.nextLine();
            equipeDAO.afficherMembresEquipe(nomConsult);
            break;
            
        case 3:
            System.out.print("Nom de l'équipe à modifier : ");
            String nomModif = scanner.nextLine();
            fr.esport.modele.Equipe eq = equipeDAO.rechercherParNom(nomModif);
            if (eq != null) {
                System.out.println("\n--- MODIFICATION DE L'ÉQUIPE (Laissez vide pour conserver) ---");
                System.out.print("Nouveau nom (" + eq.getNom() + ") : ");
                String nvNom = scanner.nextLine();
                if (!nvNom.trim().isEmpty()) eq.setNom(nvNom);
                
                System.out.print("Nouveau fichier Logo (" + eq.getLogo() + ") : ");
                String nvLogo = scanner.nextLine();
                if (!nvLogo.trim().isEmpty()) eq.setLogo(nvLogo);
                
                System.out.print("Nouveau Pays (" + eq.getPays() + ") : ");
                String nvPays = scanner.nextLine();
                if (!nvPays.trim().isEmpty()) eq.setPays(nvPays);
                
                if (equipeDAO.modifierEquipe(eq)) {
                    System.out.println("\n[Succès] L'équipe a été mise à jour !");
                }
            } else {
                System.out.println("[Erreur] Équipe introuvable.");
            }
            break;
            
        case 4:
            System.out.println("\n--- GESTION DU ROSTER (MERCATO) ---");
            System.out.print("Nom de l'équipe : ");
            fr.esport.modele.Equipe eqDest = equipeDAO.rechercherParNom(scanner.nextLine());
            System.out.print("Pseudo du joueur : ");
            fr.esport.modele.Joueur jTransfert = joueurDAO.rechercherParPseudo(scanner.nextLine());
            
            if (eqDest != null && jTransfert != null) {
                System.out.print("ID du Jeu concerné (1: LoL, 2: CS2, 3: Valorant, 4: Rocket League) : ");
                int idJeu = Integer.parseInt(scanner.nextLine());
                System.out.print("Rôle du joueur (ex: Midlaner, AWPer, Flex) : ");
                String role = scanner.nextLine();
                
                if (equipeDAO.ajouterJoueurAuRoster(eqDest.getIdEquipe(), jTransfert.getIdJoueur(), idJeu, role)) {
                    System.out.println("[Succès] Roster mis à jour ! Le joueur est assigné.");
                } else {
                    System.out.println("[Erreur] Impossible de mettre à jour le roster.");
                }
            } else {
                System.out.println("[Erreur] Équipe ou Joueur introuvable.");
            }
            break;
    }
}
private static void menuTournois() throws SQLException {
    System.out.println("\n--- GESTION DES TOURNOIS ---");
    System.out.println("1. Lister les tournois actifs");
    System.out.println("2. Inscrire une équipe à un tournoi");
    System.out.println("3. Saisir le résultat d'un match (Scores Équipes)");
    System.out.println("4. Saisir les statistiques individuelles d'un joueur");
    System.out.print("Votre choix : ");
    int choix = Integer.parseInt(scanner.nextLine());

    switch (choix) {
        case 1:
            System.out.println("\n--- LISTE DES TOURNOIS ---");
            for (fr.esport.modele.Tournoi t : tournoiDAO.listerTournois()) {
                System.out.println("- ID " + t.getIdTournoi() + " : " + t.getNom() + " [Format : " + t.getType() + " | CashPrize : " + t.getCashPrize() + "€]");
            }
            break;

        case 2:
            System.out.println("\n--- INSCRIPTION ÉQUIPE ---");
            System.out.print("ID du Tournoi : ");
            int idT = Integer.parseInt(scanner.nextLine());
            System.out.print("Nom de l'équipe à inscrire : ");
            fr.esport.modele.Equipe eq = equipeDAO.rechercherParNom(scanner.nextLine());
            
            if (eq != null) {
                if (tournoiDAO.inscrireEquipe(idT, eq.getIdEquipe())) {
                    System.out.println("[Succès] L'équipe '" + eq.getNom() + "' est inscrite au tournoi " + idT);
                }
            } else {
                System.out.println("[Erreur] Équipe introuvable.");
            }
            break;

        case 3:
            System.out.println("\n--- SAISIE DES SCORES DU MATCH ---");
            System.out.print("ID du Match (match_jeu) : ");
            int idMatch = Integer.parseInt(scanner.nextLine());
            System.out.print("Score Équipe 1 : ");
            int s1 = Integer.parseInt(scanner.nextLine());
            System.out.print("Score Équipe 2 : ");
            int s2 = Integer.parseInt(scanner.nextLine());
            
            if (tournoiDAO.enregistrerScoreMatch(idMatch, s1, s2)) {
                System.out.println("[Succès] Les scores du match ont été mis à jour.");
            } else {
                System.out.println("[Erreur] Aucun match modifié.");
            }
            break;

        case 4:
            System.out.println("\n--- SAISIE DES STATS INDIVIDUELLES (TABLE JOUER) ---");
            System.out.print("Pseudo du joueur : ");
            fr.esport.modele.Joueur j = joueurDAO.rechercherParPseudo(scanner.nextLine());
            
            if (j != null) {
                System.out.print("ID de la Manche (id_manche) : ");
                int idManche = Integer.parseInt(scanner.nextLine());
                System.out.print("Nombre de Kills : ");
                int K = Integer.parseInt(scanner.nextLine());
                System.out.print("Nombre d'Assists : ");
                int A = Integer.parseInt(scanner.nextLine());
                System.out.print("Nombre de Deaths : ");
                int D = Integer.parseInt(scanner.nextLine());
                System.out.print("Score de performance globale (ex: 8.5) : ");
                double perf = Double.parseDouble(scanner.nextLine());
                
                if (tournoiDAO.enregistrerStatsJoueur(j.getIdJoueur(), idManche, K, A, D, perf)) {
                    System.out.println("[Succès] Statistiques de " + j.getPseudo() + " enregistrées avec succès !");
                }
            } else {
                System.out.println("[Erreur] Joueur introuvable.");
            }
            break;
    }
}
private static void saisirResultatMatch() throws SQLException {
    System.out.println("\n--- SAISIE DU RÉSULTAT D'UN MATCH ---");
    System.out.print("ID du match à mettre à jour : ");
    int idMatch = Integer.parseInt(scanner.nextLine());
    System.out.print("Score de l'Équipe 1 : ");
    int score1 = Integer.parseInt(scanner.nextLine());
    System.out.print("Score de l'Équipe 2 : ");
    int score2 = Integer.parseInt(scanner.nextLine());
    
    if (tournoiDAO.enregistrerScoreMatch(idMatch, score1, score2)) {
        System.out.println("Le score de la manche a été mis à jour dans le système !");
    } else {
        System.out.println("Match introuvable ou erreur de mise à jour.");
    }
}

    private static void afficherClassementTournoi() throws SQLException {
    System.out.print("\nEntrez l'ID du tournoi à consulter : ");
    int idTournoi = Integer.parseInt(scanner.nextLine());
    tournoiDAO.afficherClassementEtMatchs(idTournoi);
}

    private static void consulterStatsJoueur() {
        System.out.print("Entrez le pseudo du joueur : ");
        String pseudo = scanner.nextLine();
        joueurDAO.afficherProfilStatistiques(pseudo); 
    }
}