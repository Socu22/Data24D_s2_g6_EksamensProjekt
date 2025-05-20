package com.g6.data24d_s2_g6_eksamensprojekt.model;

import java.sql.Date;
import java.time.LocalDate;

public class LejeAftale {
   private int aftale_Id;
   private String kunde_Navn;
   private String vognNummer;
   private String detaljer;
   private LocalDate startDato;
   private LocalDate slutDato;
   private Kunde kunde;
   private Bil bil;

    public LejeAftale(int aftale_Id, String kunde_Navn, String vognNummer, String startDato, String slutDato, String detaljer)
    {
        this(aftale_Id,kunde_Navn,vognNummer,Date.valueOf(startDato),Date.valueOf(slutDato),detaljer);
    }

    public LejeAftale(int aftale_Id, String kunde_Navn, String vognNummer, Date startDato, Date slutDato, String detaljer) {
        this.aftale_Id = aftale_Id;
        this.kunde_Navn = kunde_Navn;
        this.vognNummer = vognNummer;
        this.startDato = startDato.toLocalDate();
        this.slutDato = slutDato.toLocalDate();
        this.detaljer = detaljer;
    }

    public LejeAftale(String kunde_Navn, String vognNummer, String startDato, String slutDato, String detaljer)
    {
        this(kunde_Navn,vognNummer,Date.valueOf(startDato),Date.valueOf(slutDato),detaljer);
    }

    public LejeAftale(String kunde_Navn, String vognNummer, Date startDato, Date slutDato, String detaljer) {
        this.kunde_Navn = kunde_Navn;
        this.vognNummer = vognNummer;
        this.startDato = startDato.toLocalDate();
        this.slutDato = slutDato.toLocalDate();
        this.detaljer = detaljer;
    }

    public LejeAftale() {
    }

    public int getAftale_Id() {
        return aftale_Id;
    }

    public void setAftale_Id(int aftale_Id) {
        this.aftale_Id = aftale_Id;
    }

    public String getKunde_Navn() {
        return kunde_Navn;
    }

    public void setKunde_Navn(String kunde_Navn) {
        this.kunde_Navn = kunde_Navn;
    }

    public String getKunde_Id() {
        return getKunde_Navn();
    }// todo: den her metode skal ændres alle steder

    public void setKunde_Id(String kunde_Navn) {
        this.kunde_Navn = kunde_Navn;
    }

    public String getVognNummer() {
        return vognNummer;
    }

    public void setVognNummer(String vognNummer) {
        this.vognNummer = vognNummer;
    }

    public LocalDate getStartDato() {
        return startDato;
    }
    public LocalDate getStartLocalDate(){
        return startDato;
    }

    public void setStartDato(LocalDate startDato) {
        this.startDato = startDato;
    }

    public LocalDate getSlutDato() {
        return slutDato;
    }
    public LocalDate getSlutLocalDate(){
        return slutDato;
    }

    public void setSlutDato(LocalDate slutDato) {
        this.slutDato = slutDato;
    }

    public String getDetaljer() {
        return detaljer;
    }

    public void setDetaljer(String detaljer) {
        this.detaljer = detaljer;
    }

    public Kunde getKunde()
    {
        return kunde;
    }

    public void setKunde(Kunde kunde)
    {
        this.kunde = kunde;
    }

    public Bil getBil()
    {
        return bil;
    }

    public void setBil(Bil bil)
    {
        this.bil = bil;
    }
}
