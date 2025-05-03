package com.g6.data24d_s2_g6_eksamensprojekt.model;

public class Bruger {
    private int medArbejder_Id;
    private String navn;
    private String adganskode;
    private String stilling;
    public Bruger(int medArbejder_Id, String navn, String adganskode, String stilling) {
        this.medArbejder_Id = medArbejder_Id;
        this.navn = navn;
        this.adganskode = adganskode;
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

    public String getAdganskode() {
        return adganskode;
    }

    public void setAdganskode(String adganskode) {
        this.adganskode = adganskode;
    }

    public String getStilling() {
        return stilling;
    }

    public void setStilling(String stilling) {
        this.stilling = stilling;
    }
}
