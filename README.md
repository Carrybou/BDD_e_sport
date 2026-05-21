# BDD_e_sport
Projet de BDD plateforme E-sport

lancer le docker
docker run --name mysql-esport -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=esport -p 3306:3306 -d mysql:8.0


# 1. Création de la structure des tables (DDL)
docker exec -i mysql-esport mysql -u root -proot esport < script_creation.sql

# 2. Insertion du jeu de données massives (DML)
docker exec -i mysql-esport mysql -u root -proot esport < donnees_esport.sql

# 3. se connecter au docker
docker exec -it mysql-esport mysql -u root -proot esport

# 4. tester une commande
SELECT pseudo, nom, prenom, nationalite 
FROM joueur 
ORDER BY pseudo ASC;

afficher les match du premier tournoi
SELECT mj.date_match, e1.nom AS equipe_1, m.score_equipe1, m.score_equipe2, e2.nom AS equipe_2
FROM manche m
JOIN match_jeu mj ON m.id_match = mj.id_match
JOIN phase p ON mj.id_phase = p.id_phase
JOIN equipe e1 ON m.id_equipe1 = e1.id_equipe
JOIN equipe e2 ON m.id_equipe2 = e2.id_equipe
WHERE p.id_tournoi = 1;