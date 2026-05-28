package fr.esport.modele;

import java.sql.Date;

public class Tournoi {
    private int idTournoi;
    private String nom;
    private String jeu;
    private Date dateDebut;
    private Date dateFin;
    private String type;
    private double cashPrize;

    public Tournoi() {}
    public Tournoi(int idTournoi, String nom, String jeu, Date dateDebut, Date dateFin, String type, double cashPrize) {
        this.idTournoi = idTournoi;
        this.nom = nom;
        this.jeu = jeu;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.type = type;
        this.cashPrize = cashPrize;
    }

    // Getters / Setters
    public int getIdTournoi() { return idTournoi; }
    public void setIdTournoi(int idTournoi) { this.idTournoi = idTournoi; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getJeu() { return jeu; }
    public void setJeu(String jeu) { this.jeu = jeu; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getCashPrize() { return cashPrize; }
    public void setCashPrize(double cashPrize) { this.cashPrize = cashPrize; }
}