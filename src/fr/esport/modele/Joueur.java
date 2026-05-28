package fr.esport.modele;

import java.sql.Date;

public class Joueur {
    private int idJoueur;
    private String pseudo;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String nationalite;
    private String niveau;

    public Joueur() {}

    public Joueur(int idJoueur, String pseudo, String nom, String prenom, Date dateNaissance, String nationalite, String niveau) {
        this.idJoueur = idJoueur;
        this.pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.nationalite = nationalite;
        this.niveau = niveau;
    }

    // Getters et Setters
    public int getIdJoueur() { return idJoueur; }
    public void setIdJoueur(int idJoueur) { this.idJoueur = idJoueur; }
    public String getPseudo() { return pseudo; }
    public void setPseudo(String pseudo) { this.pseudo = pseudo; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }
    public String getNationalite() { return nationalite; }
    public void setNationalite(String nationalite) { this.nationalite = nationalite; }
    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }
}