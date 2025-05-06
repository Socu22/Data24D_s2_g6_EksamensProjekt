package com.g6.data24d_s2_g6_eksamensprojekt.model;

public class LejeAftale {
    int aftale_Id;
    int kunde_Id;
    String vognNummer;
    String startDato;
    String slutDato;
    String detaljer;

    public LejeAftale(int aftale_Id, int kunde_Id, String vognNummer, String startDato, String slutDato, String detaljer) {
        this.aftale_Id = aftale_Id;
        this.kunde_Id = kunde_Id;
        this.vognNummer = vognNummer;
        this.startDato = startDato;
        this.slutDato = slutDato;
        this.detaljer = detaljer;
    }

    public LejeAftale(int kunde_Id, String vognNummer, String startDato, String slutDato, String detaljer) {
        this.kunde_Id = kunde_Id;
        this.vognNummer = vognNummer;
        this.startDato = startDato;
        this.slutDato = slutDato;
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

    public String getStartDato() {
        return startDato;
    }

    public void setStartDato(String startDato) {
        this.startDato = startDato;
    }

    public String getSlutDato() {
        return slutDato;
    }

    public void setSlutDato(String slutDato) {
        this.slutDato = slutDato;
    }

    public String getDetaljer() {
        return detaljer;
    }

    public void setDetaljer(String detaljer) {
        this.detaljer = detaljer;
    }
}
