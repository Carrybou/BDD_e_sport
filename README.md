# BDD_e_sport
Projet de BDD plateforme E-sport

lancer le docker
docker run --name mysql-esport -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=esport -p 3306:3306 -d mysql:8.0


# 1. Création de la structure des tables (DDL)
se mettre dans le dossier database
docker exec -i mysql-esport mysql -u root -proot esport < creation_esport.sql

# 2. Insertion du jeu de données massives (DML)
docker exec -i mysql-esport mysql -u root -proot esport < creation_data_DML.sql

# 3. Création des Vues SQL

docker exec -i mysql-esport mysql -u root -proot esport < vues.sql

# 4. Se connecter au docker
docker exec -it mysql-esport mysql -u root -proot esport

# 5. tester une commande
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



Etape 2 : Configuration et Lancement de l'Application Java

1. Prérequis
JDK 17 ou supérieur installé.

Le driver JDBC mysql-connector-j-9.7.0.jar référencé dans les dépendances (si vous utilisez VS Code, ajoutez-le dans la section Referenced Libraries du menu Java Projects).

2. Compilation et Exécution (Via Terminal)

se mettre dans le dossier BDD_E_SPORT

Sur Linux / Mac :

javac -d bin -cp "lib/*" src/fr/esport/util/*.java src/fr/esport/modele/*.java src/fr/esport/dao/*.java src/fr/esport/vue/*.java
java -cp "bin:lib/*" fr.esport.vue.MenuPrincipal

Sur Windows (PowerShell) :

javac -d bin -cp "lib/*" src/fr/esport/util/*.java src/fr/esport/modele/*.java src/fr/esport/dao/*.java src/fr/esport/vue/*.java
java -cp "bin;lib/*" fr.esport.vue.MenuPrincipal