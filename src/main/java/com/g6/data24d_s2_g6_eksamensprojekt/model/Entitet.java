package com.g6.data24d_s2_g6_eksamensprojekt.model;

import org.springframework.lang.NonNull;

abstract class Entitet
{
    protected final int id;
    protected String navn;

    public Entitet(int id, @NonNull String navn)
    {
        this.id = id;
        this.navn = navn;
    }

    public String getNavn(){
        return navn;
    }
}
