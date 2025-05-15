package com.g6.data24d_s2_g6_eksamensprojekt.model;

import java.sql.Date;
import java.time.LocalDate;

public class LejeAftale {
   private int aftale_Id;
   private int kunde_Id;
   private String vognNummer;
   private String detaljer;
   private LocalDate startDato;
   private LocalDate slutDato;
   private Kunde kunde;

    public LejeAftale(int aftale_Id, int kunde_Id, String vognNummer, String startDato, String slutDato, String detaljer)
    {
        this(aftale_Id,kunde_Id,vognNummer,Date.valueOf(startDato),Date.valueOf(slutDato),detaljer);
    }

    public LejeAftale(int aftale_Id, int kunde_Id, String vognNummer, Date startDato, Date slutDato, String detaljer) {
        this.aftale_Id = aftale_Id;
        this.kunde_Id = kunde_Id;
        this.vognNummer = vognNummer;
        this.startDato = startDato.toLocalDate();
        this.slutDato = slutDato.toLocalDate();
        this.detaljer = detaljer;
    }

    public LejeAftale(int kunde_Id, String vognNummer, String startDato, String slutDato, String detaljer)
    {
        this(kunde_Id,vognNummer,Date.valueOf(startDato),Date.valueOf(slutDato),detaljer);
    }

    public LejeAftale(int kunde_Id, String vognNummer, Date startDato, Date slutDato, String detaljer) {
        this.kunde_Id = kunde_Id;
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

    public int getKunde_Id() {
        return kunde_Id;
    }

    public void setKunde_Id(int kunde_Id) {
        this.kunde_Id = kunde_Id;
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
        return LocalDate.parse(startDato);
    }

    public void setStartDato(LocalDate startDato) {
        this.startDato = startDato;
    }

    public LocalDate getSlutDato() {
        return slutDato;
    }
    public LocalDate getSlutLocalDate(){
        return LocalDate.parse(slutDato);
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
}
