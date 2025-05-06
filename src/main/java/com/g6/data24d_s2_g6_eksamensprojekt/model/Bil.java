package com.g6.data24d_s2_g6_eksamensprojekt.model;

public class Bil
{
    private final String vognNr, stelNr;
    private final BilType type;
    private int lagerId;
    private String status;

    public Bil(String vognNr, String stelNr, BilType type, int lagerId, String status)
    {
        this.vognNr = vognNr;
        this.stelNr = stelNr;
        this.type = type;
    }

    public String getVognNr()
    {
        return vognNr;
    }
}
