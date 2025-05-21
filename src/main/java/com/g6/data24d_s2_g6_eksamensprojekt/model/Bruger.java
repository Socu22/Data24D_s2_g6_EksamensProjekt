package com.g6.data24d_s2_g6_eksamensprojekt.model;

public class Bruger extends Entitet
{
    public enum Stilling
    {
        DEMO,
        DATA,
        SKADE,
        FORRETNING;
    }

    private String adgangskode;
    private Stilling stilling;

    public Bruger(int medArbejder_Id, String navn, String adgangskode, String stilling) {
        super(medArbejder_Id, navn);
        this.adgangskode = adgangskode;
        this.stilling =  Stilling.valueOf(stilling);
    }

    public int getMedArbejder_Id() {
        return id;
    }

    public String getAdgangskode() {
        return adgangskode;
    }
    public void setAdgangskode(String adgangskode) {
        this.adgangskode = adgangskode;
    }

    public String getStilling() {
        return stilling.name();
    }
    public void setStilling(String stilling) {
        this.stilling =  Stilling.valueOf(stilling);
    }
}
