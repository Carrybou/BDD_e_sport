USE esport;

-- R1. Sélection simple avec un tri ascendant standard.
SELECT pseudo, nom, prenom, nationalite 
FROM joueur 
ORDER BY pseudo ASC;

-- R2. Jointure interne pour lier le jeu au tournoi et filtre numérique simple.
SELECT t.nom AS nom_tournoi, j.nom AS nom_jeu, t.dotation 
FROM tournoi t
JOIN jeu j ON t.id_jeu = j.id_jeu
WHERE t.dotation > 10000.00;

-- R3. Multiples jointures pour lier les équipes et les scores au tournoi ciblé via ses phases.
SELECT mj.date_match, e1.nom AS equipe_1, m.score_equipe1, m.score_equipe2, e2.nom AS equipe_2
FROM manche m
JOIN match_jeu mj ON m.id_match = mj.id_match
JOIN phase p ON mj.id_phase = p.id_phase
JOIN equipe e1 ON m.id_equipe1 = e1.id_equipe
JOIN equipe e2 ON m.id_equipe2 = e2.id_equipe
WHERE p.id_tournoi = 1;

-- R4. Utilisation de la table de liaison 'roster' pour filtrer les appartenances selon l'ID du jeu choisi.
SELECT e.nom AS nom_equipe, j.pseudo, r.role_joueur
FROM roster r
JOIN equipe e ON r.id_equipe = e.id_equipe
JOIN joueur j ON r.id_joueur = j.id_joueur
WHERE r.id_jeu = 1
ORDER BY e.nom, j.pseudo;

-- R5. Extraction de l'équipe ayant le score le plus élevé lors de la phase finale des tournois clos.
SELECT 
    t.nom AS nom_tournoi, 
    p.nom AS nom_phase,
    mj.id_match,
    e1.nom AS equipe_1,
    e2.nom AS equipe_2,
    CASE 
        WHEN m.score_equipe1 > m.score_equipe2 THEN e1.nom
        ELSE e2.nom
    END AS equipe_gagnante
FROM tournoi t
JOIN phase p ON t.id_tournoi = p.id_tournoi
JOIN match_jeu mj ON p.id_phase = mj.id_phase
JOIN manche m ON mj.id_match = m.id_match
JOIN equipe e1 ON m.id_equipe1 = e1.id_equipe
JOIN equipe e2 ON m.id_equipe2 = e2.id_equipe;

-- R6. Utilisation de structures conditionnelles (CASE WHEN) dans un calcul de somme pour ventiler les statistiques par équipe.
SELECT m.id_match, e1.nom AS equipe_1,
    SUM(CASE WHEN r.id_equipe = m.id_equipe1 THEN s.kills ELSE 0 END) AS total_kills_equipe1,
    SUM(CASE WHEN r.id_equipe = m.id_equipe1 THEN s.deaths ELSE 0 END) AS total_deaths_equipe1,
    e2.nom AS equipe_2,
    SUM(CASE WHEN r.id_equipe = m.id_equipe2 THEN s.kills ELSE 0 END) AS total_kills_equipe2,
    SUM(CASE WHEN r.id_equipe = m.id_equipe2 THEN s.deaths ELSE 0 END) AS total_deaths_equipe2
FROM manche m
JOIN jouer s ON m.id_manche = s.id_manche
JOIN match_jeu mj ON m.id_match = mj.id_match
JOIN phase p ON mj.id_phase = p.id_phase
JOIN tournoi t ON p.id_tournoi = t.id_tournoi
JOIN roster r ON s.id_joueur = r.id_joueur AND r.id_jeu = t.id_jeu
JOIN equipe e1 ON m.id_equipe1 = e1.id_equipe
JOIN equipe e2 ON m.id_equipe2 = e2.id_equipe
GROUP BY m.id_match, e1.nom, e2.nom;

-- R7. Groupement par identifiant de jeu associé à une fonction de décompte COUNT.
SELECT j.nom AS nom_jeu, COUNT(t.id_tournoi) AS nombre_tournois
FROM jeu j
LEFT JOIN tournoi t ON j.id_jeu = t.id_jeu
GROUP BY j.id_jeu, j.nom
ORDER BY nombre_tournois DESC;

-- R8. Filtrage post-groupement avec la clause HAVING COUNT(DISTINCT...) pour isoler le cumul d'activités.
SELECT e.nom AS nom_equipe, COUNT(DISTINCT p.id_tournoi) AS nb_tournois_participes
FROM equipe e
JOIN manche m ON (e.id_equipe = m.id_equipe1 OR e.id_equipe = m.id_equipe2)
JOIN match_jeu mj ON m.id_match = mj.id_match
JOIN phase p ON mj.id_phase = p.id_phase
GROUP BY e.id_equipe, e.nom
HAVING COUNT(DISTINCT p.id_tournoi) >= 2;

-- R9. Division des totaux avec protection anti-division par zéro via NULLIF, restreint par une condition HAVING de volume.
SELECT j.pseudo, SUM(s.kills) AS total_kills, SUM(s.deaths) AS total_deaths,
    SUM(s.kills) / NULLIF(SUM(s.deaths), 0) AS KDA_ratio_moyen
FROM joueur j
JOIN jouer s ON j.id_joueur = s.id_joueur
GROUP BY j.id_joueur, j.pseudo
HAVING COUNT(s.id_manche) >= 1
ORDER BY KDA_ratio_moyen DESC
LIMIT 1;

-- R10. Regroupement multi-niveaux (tournoi puis équipe) combiné à l'utilisation de la moyenne AVG.
SELECT t.nom AS nom_tournoi, e.nom AS nom_equipe, AVG(s.score_performance) AS moyenne_performance
FROM jouer s
JOIN manche m ON s.id_manche = m.id_manche
JOIN match_jeu mj ON m.id_match = mj.id_match
JOIN phase p ON mj.id_phase = p.id_phase
JOIN tournoi t ON p.id_tournoi = t.id_tournoi
JOIN roster r ON s.id_joueur = r.id_joueur AND r.id_jeu = t.id_jeu
JOIN equipe e ON r.id_equipe = e.id_equipe
GROUP BY t.id_tournoi, t.nom, e.id_equipe, e.nom
ORDER BY nom_tournoi, moyenne_performance DESC;

-- R11. Utilisation de la condition NOT EXISTS corrélée pour exclure les correspondances avec le format ciblé.
SELECT j.id_joueur, j.pseudo, j.nationalite
FROM joueur j
WHERE NOT EXISTS (
    SELECT 1 FROM jouer s
    JOIN manche m ON s.id_manche = m.id_manche
    JOIN match_jeu mj ON m.id_match = mj.id_match
    JOIN phase p ON mj.id_phase = p.id_phase
    JOIN tournoi t ON p.id_tournoi = t.id_tournoi
    WHERE s.id_joueur = j.id_joueur AND t.type_format = 'en ligne'
);

-- R12. Double condition (EXISTS pour valider la présence et NOT EXISTS pour vérifier l'absence d'une défaite ou d'un nul).
SELECT e.nom AS equipe_invaincue_poule
FROM equipe e
JOIN roster r ON e.id_equipe = r.id_equipe
JOIN tournoi t ON r.id_jeu = t.id_jeu
WHERE t.id_tournoi = 1
  AND EXISTS (
      SELECT 1 FROM manche m 
      JOIN match_jeu mj ON m.id_match = mj.id_match
      JOIN phase p ON mj.id_phase = p.id_phase
      WHERE p.id_tournoi = 1 AND p.nom LIKE '%Group%' 
        AND (m.id_equipe1 = e.id_equipe OR m.id_equipe2 = e.id_equipe)
  )
  AND NOT EXISTS (
      SELECT 1 FROM manche m
      JOIN match_jeu mj ON m.id_match = mj.id_match
      JOIN phase p ON mj.id_phase = p.id_phase
      WHERE p.id_tournoi = 1 AND p.nom LIKE '%Group%'
        AND (
            (m.id_equipe1 = e.id_equipe AND m.score_equipe1 <= m.score_equipe2) OR
            (m.id_equipe2 = e.id_equipe AND m.score_equipe2 <= m.score_equipe1)
        )
  );

-- R13. Utilisation d'une table temporaire (CTE) et de la fonction analytique de fenêtrage DENSE_RANK() pour gérer les égalités.
WITH bilan_equipes AS (
    SELECT e.nom AS nom_equipe,
        SUM(CASE WHEN (m.id_equipe1 = e.id_equipe AND m.score_equipe1 > m.score_equipe2) OR (m.id_equipe2 = e.id_equipe AND m.score_equipe2 > m.score_equipe1) THEN 1 ELSE 0 END) AS total_victoires,
        SUM(CASE WHEN m.id_equipe1 = e.id_equipe THEN (m.score_equipe1 - m.score_equipe2) WHEN m.id_equipe2 = e.id_equipe THEN (m.score_equipe2 - m.score_equipe1) ELSE 0 END) AS diff_rounds
    FROM equipe e
    JOIN manche m ON (e.id_equipe = m.id_equipe1 OR e.id_equipe = m.id_equipe2)
    JOIN match_jeu mj ON m.id_match = mj.id_match
    JOIN phase p ON mj.id_phase = p.id_phase
    WHERE p.id_tournoi = 1
    GROUP BY e.id_equipe, e.nom
)
SELECT nom_equipe, total_victoires, diff_rounds,
    DENSE_RANK() OVER (ORDER BY total_victoires DESC, diff_rounds DESC) AS rang_classement
FROM bilan_equipes;

-- R14. Utilisation de COUNT(DISTINCT...) dans la clause restrictive HAVING pour filtrer la pluralité des clés de jeu.
SELECT j.id_joueur, j.pseudo, COUNT(DISTINCT r.id_jeu) AS nb_jeux_differents
FROM joueur j
JOIN roster r ON j.id_joueur = r.id_joueur
GROUP BY j.id_joueur, j.pseudo
HAVING COUNT(DISTINCT r.id_jeu) >= 2;

-- R15. Partitionnement analytique avec RANK() pour isoler de manière autonome le premier rang de chaque sous-ensemble de jeu.
WITH total_kills_par_jeu AS (
    SELECT j.id_jeu, j.nom AS nom_jeu, jr.pseudo, SUM(s.kills) AS total_kills,
        RANK() OVER (PARTITION BY j.id_jeu ORDER BY SUM(s.kills) DESC) AS rang_buteur
    FROM jouer s
    JOIN manche m ON s.id_manche = m.id_manche
    JOIN match_jeu mj ON m.id_match = mj.id_match
    JOIN phase p ON mj.id_phase = p.id_phase
    JOIN tournoi t ON p.id_tournoi = t.id_tournoi
    JOIN jeu j ON t.id_jeu = j.id_jeu
    JOIN joueur jr ON s.id_joueur = jr.id_joueur
    GROUP BY j.id_jeu, j.nom, jr.id_joueur, jr.pseudo
)
SELECT nom_jeu, pseudo AS meilleur_buteur, total_kills
FROM total_kills_par_jeu
WHERE rang_buteur = 1;