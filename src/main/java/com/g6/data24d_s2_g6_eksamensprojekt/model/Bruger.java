package com.g6.data24d_s2_g6_eksamensprojekt.model;

public class Bruger {
    private int medArbejder_Id;
    private String navn;
    private String adgangskode;
    private String stilling;
    public Bruger(int medArbejder_Id, String navn, String adgangskode, String stilling) {
        this.medArbejder_Id = medArbejder_Id;
        this.navn = navn;
        this.adgangskode = adgangskode;
        this.stilling = stilling;
    }

    public int getMedArbejder_Id() {
        return medArbejder_Id;
    }

    public void setMedArbejder_Id(int medArbejder_Id) {
        this.medArbejder_Id = medArbejder_Id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getAdgangskode() {
        return adgangskode;
    }

    public void setAdgangskode(String adgangskode) {
        this.adgangskode = adgangskode;
    }

    public String getStilling() {
        return stilling;
    }

    public void setStilling(String stilling) {
        this.stilling = stilling;
    }
}
