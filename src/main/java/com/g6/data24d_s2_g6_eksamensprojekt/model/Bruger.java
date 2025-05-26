package com.g6.data24d_s2_g6_eksamensprojekt.model;

public class Bruger extends Person
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
        this.stilling =  Stilling.valueOf(stilling.toUpperCase());
    }

    public int getMedArbejder_Id() {
        return id;
    }

    public String getAdgangskode() {
        return adgangskode;
    }
    public void   setAdgangskode(String adgangskode) {
        this.adgangskode = adgangskode;
    }

    public String getStilling() {
        return stilling.name();
    }
    public void   setStilling(String stilling) {
        this.stilling =  Stilling.valueOf(stilling.toUpperCase());
    }

    public boolean erStilling(Stilling... tilladteStillinger)
    {
        if (this.stilling == Stilling.DEMO || tilladteStillinger.length == 0) return true;
        for (Stilling stilling : tilladteStillinger)
        {
            if (this.stilling == stilling) return true;
        }
        return false;
    }

    public boolean erStilling(String... tilladteStillinger)
    {
        if (this.stilling == Stilling.DEMO || tilladteStillinger.length == 0) return true;
        for (String stilling : tilladteStillinger)
        {
            if (this.stilling.name().equalsIgnoreCase(stilling)) return true;
        }
        return false;
    }
}
