package com.g6.data24d_s2_g6_eksamensprojekt.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bil
{
    public enum Status
    {
        TILGAENGELIG,
        LIMITED, UNLIMITED,
        TILBAGELEVERET,
        SKADET,
        AFHENTNINGSKLAR, // Denne status indikerer at bilen er klar til transport/afhentning
        SOLGT;

        public static List<String> getNames()
        {
            ArrayList<String> s = new ArrayList<>();
            for (Status status : Status.values())
            {
                s.add(status.name());
            }
            return s;
        }
    }

    private final String vognNummer, stelNummer;
    private BilType type;
    private int lager_Id;
    private Status status;
    private double kørteKm;
    private Lager lager;
    private List<Notation> notationer;
    private LejeAftale lejeAftale;
    private LejeAftale koebsAftale; // todo:


    public Bil(String vognNummer, String stelNummer, BilType type, int lager_Id, String status,double kørteKm) {
        this.vognNummer = vognNummer;
        this.stelNummer = stelNummer;
        this.type = type;
        this.lager_Id = lager_Id;
        this.status = Status.valueOf(status);
        this.kørteKm=kørteKm;
    }
    public Bil(String vognNummer, String stelNummer, BilType type, int lager_Id, String status) {
        this.vognNummer = vognNummer;
        this.stelNummer = stelNummer;
        this.type = type;
        this.lager_Id = lager_Id;
        this.status = Status.valueOf(status);

    }

    public double getKørteKm() {
        return kørteKm;
    }

    public void setKørteKm(double kørteKm) {
        this.kørteKm = kørteKm;
    }

    public String getVognNummer() {
        return vognNummer;
    }

    public String getStelNummer() {
        return stelNummer;
    }

    public BilType getType() {
        return type;
    }

    public void setType(BilType type)
    {
        this.type = type;
    }

    public int getLager_Id() {
        return lager_Id;
    }

    public void setLager_Id(int lager_Id) {
        this.lager_Id = lager_Id;
    }

    public String getStatus() {
        return status.name();
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status);
    }

    public boolean erStatus(String... status)
    {
        for (String s : status)
        {
            if (this.status.name().equalsIgnoreCase(s)) return true;
        }
        return false;
    }

    public boolean erTilgaengelig()
    {
        return erStatus("TILGAENGELIG");
    }

    public int getBilType_Id(){
        return type.getBilType_Id();
    }

    public Lager getLager()
    {
        return lager;
    }

    public void setLager(Lager lager)
    {
        this.lager = lager;
    }

    public void setLejeAftale(LejeAftale lejeAftale) {
        this.lejeAftale = lejeAftale;
    }

    public LejeAftale getLejeAftale() {
        return lejeAftale;
    }

    public String toString()
    {
        StringBuilder s = new StringBuilder(vognNummer);
        s.insert(4,' ').insert(2,' ');
        return s.toString();
    }
}
