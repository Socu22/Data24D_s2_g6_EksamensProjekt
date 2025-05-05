package com.g6.data24d_s2_g6_eksamensprojekt.model;

public class Lager {
    int lager_Id;
    String navn;
    String adresse;

    public Lager(int lager_Id, String navn, String adresse) {
        this.lager_Id = lager_Id;
        this.navn = navn;
        this.adresse = adresse;
    }
    public Lager(String navn, String adresse) {
        this.navn = navn;
        this.adresse = adresse;
    }

    public Lager() {
    }

    public int getLager_Id() {
        return lager_Id;
    }

    public void setLager_Id(int lager_Id) {
        this.lager_Id = lager_Id;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
}
