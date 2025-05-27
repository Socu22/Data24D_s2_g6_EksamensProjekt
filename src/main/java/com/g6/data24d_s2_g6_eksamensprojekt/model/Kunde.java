package com.g6.data24d_s2_g6_eksamensprojekt.model;

public class Kunde extends Person// !OutDated
{
    public Kunde(int kundeId, String navn)
    {
        super(kundeId, navn);
    }

    public int getKunde_Id() {
        return id;
    }
}
