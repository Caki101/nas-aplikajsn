package com.backend.Backend.dataTypes;

import java.sql.Timestamp;

public class TiketDDSO { // developer data saving object
    private Smestaj smestaj;
    private String sezona;
    private Double cena;
    private Integer trajanje_odmora;
    private Integer broj_osoba;
    private Integer broj_tiketa;
    private String prevoz;
    private Timestamp polazak;

    public TiketDDSO() {
    }

    @Override
    public String toString() {
        return "TiketDDSO{" +
                "smestaj=" + smestaj.toString() +
                ", sezona='" + sezona + '\'' +
                ", cena=" + cena +
                ", trajanje_odmora=" + trajanje_odmora +
                ", broj_osoba=" + broj_osoba +
                ", broj_tiketa=" + broj_tiketa +
                ", prevoz='" + prevoz + '\'' +
                ", polazak=" + polazak +
                '}';
    }

    public Smestaj getSmestaj() {
        return smestaj;
    }

    public void setSmestaj(Smestaj smestaj) {
        this.smestaj = smestaj;
    }

    public String getSezona() {
        return sezona;
    }

    public void setSezona(String sezona) {
        this.sezona = sezona;
    }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }

    public Integer getTrajanje_odmora() {
        return trajanje_odmora;
    }

    public void setTrajanje_odmora(Integer trajanje_odmora) {
        this.trajanje_odmora = trajanje_odmora;
    }

    public Integer getBroj_osoba() {
        return broj_osoba;
    }

    public void setBroj_osoba(Integer broj_osoba) {
        this.broj_osoba = broj_osoba;
    }

    public Integer getBroj_tiketa() {
        return broj_tiketa;
    }

    public void setBroj_tiketa(Integer broj_tiketa) {
        this.broj_tiketa = broj_tiketa;
    }

    public String getPrevoz() {
        return prevoz;
    }

    public void setPrevoz(String prevoz) {
        this.prevoz = prevoz;
    }

    public Timestamp getPolazak() {
        return polazak;
    }

    public void setPolazak(Timestamp polazak) {
        this.polazak = polazak;
    }
}
