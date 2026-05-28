package fr.esport.modele;

import java.sql.Date;

public class Equipe {
    private int idEquipe;
    private String nom;
    private String logo;
    private Date dateCreation;
    private String pays;

    public Equipe() {}
    public Equipe(int idEquipe, String nom, String logo, Date dateCreation, String pays) {
        this.idEquipe = idEquipe;
        this.nom = nom;
        this.logo = logo;
        this.dateCreation = dateCreation;
        this.pays = pays;
    }

    // Getters / Setters
    public int getIdEquipe() { return idEquipe; }
    public void setIdEquipe(int idEquipe) { this.idEquipe = idEquipe; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    public Date getDateCreation() { return dateCreation; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }
    public String getPays() { return pays; }
    public void setPays(String pays) { this.pays = pays; }
}