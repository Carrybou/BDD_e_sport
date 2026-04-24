USE esport;

-- 1. JEUX (4)
INSERT INTO jeu (nom, genre, editeur, annee_sortie) VALUES 
('League of Legends', 'MOBA', 'Riot Games', 2009),
('Counter-Strike 2', 'FPS', 'Valve', 2023),
('Valorant', 'FPS', 'Riot Games', 2020),
('Rocket League', 'Sport', 'Psyonix', 2015);

-- 2. EQUIPES (6)
INSERT INTO equipe (nom, logo, date_creation, pays) VALUES 
('Karmine Corp', 'kc.png', '2020-03-30', 'France'),
('G2 Esports', 'g2.png', '2015-10-15', 'Allemagne'),
('Team Vitality', 'vit.png', '2013-08-05', 'France'),
('Fnatic', 'fnc.png', '2004-07-23', 'Royaume-Uni'),
('Natus Vincere', 'navi.png', '2009-12-17', 'Ukraine'),
('T1', 't1.png', '2003-04-13', 'Corée du Sud');

-- 3. JOUEURS (20)
INSERT INTO joueur (pseudo, nom, prenom, date_naissance, nationalite, niveau) VALUES 
('Faker', 'Lee', 'Sang-hyeok', '1996-05-07', 'Corée du Sud', 'Légende'),
('Caps', 'Winther', 'Rasmus', '1999-11-17', 'Danemark', 'Elite'),
('ZywOo', 'Herbaut', 'Mathieu', '2000-11-09', 'France', 'Elite'),
('Saken', 'Kradra', 'Lucas', '1998-11-22', 'France', 'Pro'),
('s1mple', 'Kostyliev', 'Oleksandr', '1997-10-02', 'Ukraine', 'Elite'),
('TenZ', 'Ngo', 'Tyson', '2001-05-05', 'Canada', 'Pro'),
('Boaster', 'Robertson', 'Jake', '1995-05-25', 'Royaume-Uni', 'Pro'),
('Vatira', 'Farès', 'Axel', '2006-05-10', 'France', 'Elite'),
('Zen', 'Bernier', 'Alexis', '2007-02-20', 'France', 'Elite'),
('Hans Sama', 'Duchevallier', 'Steven', '1999-09-02', 'France', 'Pro'),
('Mikyx', 'Mehle', 'Mihael', '1998-11-02', 'Slovénie', 'Pro'),
('NiKo', 'Kovač', 'Nikola', '1997-02-16', 'Bosnie', 'Elite'),
('m0NESY', 'Osipov', 'Ilya', '2005-05-01', 'Russie', 'Pro'),
('ScreaM', 'Benrlitom', 'Adil', '1994-07-02', 'Belgique', 'Vétéran'),
('ShowMaker', 'Heo', 'Su', '2000-07-22', 'Corée du Sud', 'Elite'),
('Caliste', 'Henry-Hennebert', 'Caliste', '2006-08-28', 'France', 'Espoir'),
('Rekkles', 'Larsson', 'Martin', '1996-09-20', 'Suède', 'Vétéran'),
('apEX', 'Madesclaire', 'Dan', '1993-02-22', 'France', 'Pro'),
('shox', 'Papillon', 'Richard', '1992-05-27', 'France', 'Vétéran'),
('BeryL', 'Cho', 'Geon-hee', '1997-04-01', 'Corée du Sud', 'Pro');

-- 4. TOURNOIS (3)
INSERT INTO tournoi (nom, date_debut, date_fin, type_format, dotation, statut, id_jeu) VALUES 
('Worlds 2024', '2024-10-01 14:00:00', '2024-11-02 20:00:00', 'LAN', 2225000.00, 'Terminé', 1),
('PGL Major Copenhagen', '2024-03-17 10:00:00', '2024-03-31 22:00:00', 'LAN', 1250000.00, 'Terminé', 2),
('RLCS World Championship', '2024-09-10 12:00:00', '2024-09-15 19:00:00', 'LAN', 1100000.00, 'Terminé', 4);

-- 5. PHASES (9 - 3 par tournoi)
INSERT INTO phase (nom, id_tournoi) VALUES 
('Play-In', 1), ('Group Stage', 1), ('Knockout Phase', 1),
('Challengers Stage', 2), ('Legends Stage', 2), ('Champions Stage', 2),
('Wildcard', 3), ('Group Stage', 3), ('Main Event', 3);

-- 6. MATCH_JEU (15)
-- Distribution sur les phases des tournois
INSERT INTO match_jeu (date_match, id_phase) VALUES 
('2024-10-05 15:00:00', 2), ('2024-10-06 18:00:00', 2), ('2024-11-02 14:00:00', 3), -- T1 LoL
('2024-03-20 12:00:00', 5), ('2024-03-21 15:00:00', 5), ('2024-03-31 20:00:00', 6), -- CS2
('2024-09-12 10:00:00', 8), ('2024-09-13 13:00:00', 8), ('2024-09-15 18:00:00', 9), -- RL
('2024-10-10 17:00:00', 2), ('2024-10-12 17:00:00', 2), ('2024-10-15 17:00:00', 2),
('2024-03-25 10:00:00', 5), ('2024-03-26 13:00:00', 5), ('2024-03-27 16:00:00', 5);

-- 7. MANCHES (15 - 1 par match pour simplifier l'exemple)
INSERT INTO manche (nom_map, score_equipe1, score_equipe2, id_equipe1, id_equipe2, id_match) VALUES 
('Summoners Rift', 1, 0, 6, 2, 1), ('Summoners Rift', 1, 0, 3, 4, 2), ('Summoners Rift', 3, 2, 6, 4, 3),
('Mirage', 13, 11, 5, 3, 4), ('Inferno', 13, 7, 2, 5, 5), ('Nuke', 2, 1, 5, 2, 6),
('Mannfield', 4, 1, 3, 1, 7), ('DFH Stadium', 3, 2, 2, 1, 8), ('Champions Field', 4, 0, 3, 2, 9),
('Summoners Rift', 1, 0, 6, 1, 10), ('Summoners Rift', 0, 1, 2, 1, 11), ('Summoners Rift', 1, 0, 4, 1, 12),
('Ancient', 13, 5, 3, 1, 13), ('Anubis', 13, 10, 5, 1, 14), ('Overpass', 16, 14, 2, 1, 15);

-- 8. JOUER (60 stats)
-- Pour chaque manche, on insère les stats des joueurs clés
-- Ici on simule 4 joueurs par manche sur les 15 manches = 60 entrées
INSERT INTO jouer (id_joueur, id_manche, kills, assists, deaths, score_performance)
SELECT j.id_joueur, m.id_manche, FLOOR(RAND()*25), FLOOR(RAND()*15), FLOOR(RAND()*20), RAND()*10
FROM joueur j, manche m
WHERE (j.id_joueur + m.id_manche) % 5 = 0 -- Algorithme simple pour distribuer 60 entrées
LIMIT 60;