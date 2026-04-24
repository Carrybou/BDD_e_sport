CREATE DATABASE IF NOT EXISTS esport;
USE esport;

-- Suppression des tables dans l'ordre inverse des dépendances pour éviter les erreurs
DROP TABLE IF EXISTS jouer;
DROP TABLE IF EXISTS roster;
DROP TABLE IF EXISTS manche;
DROP TABLE IF EXISTS match_jeu;
DROP TABLE IF EXISTS phase;
DROP TABLE IF EXISTS tournoi;
DROP TABLE IF EXISTS joueur;
DROP TABLE IF EXISTS equipe;
DROP TABLE IF EXISTS jeu;

-- 1. Table JEU (On reste au singulier)
CREATE TABLE jeu (
    id_jeu INT AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    genre VARCHAR(50),
    editeur VARCHAR(50),
    annee_sortie INT, -- Plus simple pour les calculs d'ancienneté
    PRIMARY KEY (id_jeu)
) ENGINE=InnoDB;

-- 2. Table EQUIPE
CREATE TABLE equipe (
    id_equipe INT AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    logo VARCHAR(100),
    date_creation DATE,
    pays VARCHAR(50),
    PRIMARY KEY (id_equipe)
) ENGINE=InnoDB;

-- 3. Table JOUEUR
CREATE TABLE joueur (
    id_joueur INT AUTO_INCREMENT,
    pseudo VARCHAR(50) NOT NULL,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    date_naissance DATE,
    nationalite VARCHAR(50) NOT NULL,
    niveau VARCHAR(50),
    PRIMARY KEY (id_joueur),
    UNIQUE (pseudo)
) ENGINE=InnoDB;

-- 4. Table TOURNOI
CREATE TABLE tournoi (
    id_tournoi INT AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    date_debut DATETIME,
    date_fin DATETIME,
    type_format VARCHAR(50) NOT NULL, -- 'type' est parfois réservé, on utilise type_format
    dotation DECIMAL(19,4),
    statut ENUM('À venir', 'En cours', 'Terminé'),
    id_jeu INT NOT NULL,
    PRIMARY KEY (id_tournoi),
    CONSTRAINT fk_tournoi_jeu FOREIGN KEY (id_jeu) REFERENCES jeu(id_jeu)
) ENGINE=InnoDB;

-- 5. Table PHASE
CREATE TABLE phase (
    id_phase INT AUTO_INCREMENT,
    nom VARCHAR(50),
    id_tournoi INT NOT NULL,
    PRIMARY KEY (id_phase),
    CONSTRAINT fk_phase_tournoi FOREIGN KEY (id_tournoi) REFERENCES tournoi(id_tournoi) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 6. Table MATCH_JEU (Match est un mot réservé en SQL, on ajoute un suffixe)
CREATE TABLE match_jeu (
    id_match INT AUTO_INCREMENT,
    date_match DATETIME,
    id_phase INT NOT NULL,
    PRIMARY KEY (id_match),
    CONSTRAINT fk_match_phase FOREIGN KEY (id_phase) REFERENCES phase(id_phase) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 7. Table MANCHE (Détails des maps dans un match)
CREATE TABLE manche (
    id_manche INT AUTO_INCREMENT,
    nom_map VARCHAR(50),
    score_equipe1 INT DEFAULT 0,
    score_equipe2 INT DEFAULT 0,
    id_equipe1 INT NOT NULL,
    id_equipe2 INT NOT NULL,
    id_match INT NOT NULL,
    PRIMARY KEY (id_manche),
    CONSTRAINT fk_manche_equipe1 FOREIGN KEY (id_equipe1) REFERENCES equipe(id_equipe),
    CONSTRAINT fk_manche_equipe2 FOREIGN KEY (id_equipe2) REFERENCES equipe(id_equipe),
    CONSTRAINT fk_manche_match FOREIGN KEY (id_match) REFERENCES match_jeu(id_match) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 8. Table ROSTER (Association Ternaire : Joueur + Equipe + Jeu)
CREATE TABLE roster (
    id_equipe INT,
    id_joueur INT,
    id_jeu INT,
    date_debut DATE,
    role_joueur VARCHAR(50),
    PRIMARY KEY (id_equipe, id_joueur, id_jeu),
    CONSTRAINT fk_roster_equipe FOREIGN KEY (id_equipe) REFERENCES equipe(id_equipe),
    CONSTRAINT fk_roster_joueur FOREIGN KEY (id_joueur) REFERENCES joueur(id_joueur),
    CONSTRAINT fk_roster_jeu FOREIGN KEY (id_jeu) REFERENCES jeu(id_jeu)
) ENGINE=InnoDB;

-- 9. Table jouer (Remplace 'jouer' pour plus de clarté)
CREATE TABLE jouer (
    id_joueur INT,
    id_manche INT,
    kills INT DEFAULT 0,
    assists INT DEFAULT 0,
    deaths INT DEFAULT 0,
    score_performance DOUBLE,
    PRIMARY KEY (id_joueur, id_manche),
    CONSTRAINT fk_stat_joueur FOREIGN KEY (id_joueur) REFERENCES joueur(id_joueur),
    CONSTRAINT fk_stat_manche FOREIGN KEY (id_manche) REFERENCES manche(id_manche) ON DELETE CASCADE
) ENGINE=InnoDB;