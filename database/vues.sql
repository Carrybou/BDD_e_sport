
USE esport;

-- V1. VUE CLASSEMENT GÉNÉRAL ET STATISTIQUES DES JOUEURS
CREATE OR REPLACE VIEW vue_general_stats_joueurs AS
SELECT 
    j.id_joueur,
    j.pseudo,
    j.nationalite,
    COUNT(s.id_manche) AS matchs_joues,
    SUM(s.kills) AS total_kills,
    SUM(s.deaths) AS total_deaths,
    ROUND(SUM(s.kills) / NULLIF(SUM(s.deaths), 0), 2) AS kda_global
FROM joueur j
LEFT JOIN jouer s ON j.id_joueur = s.id_joueur
GROUP BY j.id_joueur, j.pseudo, j.nationalite;


-- V2. VUE SUIVI DES ROSTERS ACTIFS
CREATE OR REPLACE VIEW vue_rosters_actifs AS
SELECT 
    e.id_equipe,
    e.nom AS nom_equipe,
    j.id_joueur,
    j.pseudo AS pseudo_joueur,
    je.id_jeu,
    je.nom AS nom_jeu,
    r.role_joueur
FROM roster r
JOIN equipe e ON r.id_equipe = e.id_equipe
JOIN joueur j ON r.id_joueur = j.id_joueur
JOIN jeu je ON r.id_jeu = je.id_jeu;


-- V3. VUE HISTORIQUE ET RÉSULTATS DES MATCHS

CREATE OR REPLACE VIEW vue_resultats_matchs AS
SELECT 
    t.id_tournoi,
    t.nom AS nom_tournoi,
    p.nom AS nom_phase,
    mj.id_match,
    mj.date_match,
    e1.id_equipe AS id_equipe1,
    e1.nom AS equipe_1,
    m.score_equipe1,
    m.score_equipe2,
    e2.id_equipe AS id_equipe2,
    e2.nom AS equipe_2
FROM match_jeu mj
JOIN phase p ON mj.id_phase = p.id_phase
JOIN tournoi t ON p.id_tournoi = t.id_tournoi
JOIN manche m ON mj.id_match = m.id_match
JOIN equipe e1 ON m.id_equipe1 = e1.id_equipe
JOIN equipe e2 ON m.id_equipe2 = e2.id_equipe;
