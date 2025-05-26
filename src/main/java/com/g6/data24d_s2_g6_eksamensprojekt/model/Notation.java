package com.g6.data24d_s2_g6_eksamensprojekt.model;

public class Notation
{
    private final Integer id, aftaleId; // aftaleId er ikke primitiv da denne kan være null
    private final String vognNummer;
    private String beskrivelse;
    private double pris;

    public Notation(int id, Integer aftaleId, String vognNummer, String beskrivelse, double pris)
    {
        this.id = id;
        this.vognNummer = vognNummer;
        this.aftaleId = aftaleId;
        this.beskrivelse = beskrivelse;
        this.pris = pris;
    }

    public int getId() {return id;}
    public int getAftaleId() {return aftaleId;}
    public String getVognNummer() {return vognNummer;}

    public String getBeskrivelse() {return beskrivelse;}
    public double getPris() {return pris;}

    public void setBeskrivelse(String beskrivelse) {this.beskrivelse = beskrivelse;}
    public void setPris(double pris) {this.pris = pris;}
}
