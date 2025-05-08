package com.g6.data24d_s2_g6_eksamensprojekt.model;

public class Notation
{
    private final Integer id, vognNr, aftaleId;
    private String beskrivelse;
    private double pris;

    public Notation(int id, int vognNr, Integer aftaleId, String beskrivelse, double pris)
    {
        this.id = id;
        this.vognNr = vognNr;
        this.aftaleId = aftaleId;
        this.beskrivelse = beskrivelse;
        this.pris = pris;
    }

    public int getId() {return id;}
    public int getAftaleId() {return aftaleId;}
    public int getVognNr() {return vognNr;}

    public String getBeskrivelse() {return beskrivelse;}
    public double getPris() {return pris;}

    public void setBeskrivelse(String beskrivelse) {this.beskrivelse = beskrivelse;}
    public void setPris(double pris) {this.pris = pris;}

    public String toString() {return "Aftale " + aftaleId + ": " + beskrivelse;}
}
