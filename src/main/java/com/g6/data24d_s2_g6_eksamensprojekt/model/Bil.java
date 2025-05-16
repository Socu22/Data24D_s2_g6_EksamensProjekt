package com.g6.data24d_s2_g6_eksamensprojekt.model;

import java.util.List;
import java.util.Objects;

public class Bil
{
    private final String vognNummer, stelNummer;
    private BilType type;
    private int lager_Id;
    private String status;
    private List<Notation> notationer;
    private List<LejeAftale> lejeAftaler;


    public Bil(String vognNummer, String stelNummer, BilType type, int lager_Id, String status) {
        this.vognNummer = vognNummer;
        this.stelNummer = stelNummer;
        this.type = type;
        this.lager_Id = lager_Id;
        this.status = status;
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
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBilType_Id(){
        return type.getBilType_Id();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Bil bil)) return false;
        return getLager_Id() == bil.getLager_Id() && Objects.equals(getVognNummer(), bil.getVognNummer()) && Objects.equals(getStelNummer(), bil.getStelNummer()) && Objects.equals(getType(), bil.getType()) && Objects.equals(getStatus(), bil.getStatus()) && Objects.equals(notationer, bil.notationer) && Objects.equals(lejeAftaler, bil.lejeAftaler);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVognNummer(), getStelNummer(), getType(), getLager_Id(), getStatus(), notationer, lejeAftaler);
    }

    public String toString()
    {
        StringBuilder s = new StringBuilder(vognNummer);
        s.insert(4,' ').insert(2,' ');
        return s.toString();
    }
}
