package com.backend.Backend.dataTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class TiketDTO {

    @JsonProperty("smestaj_id")
    private Long smestaj_id;
    private String sezona;
    private Double cena;
    private Integer trajanje_odmora;
    private Integer broj_osoba;
    private Integer broj_tiketa;
    private String prevoz;
    private Timestamp polazak;

    public Long getSmestaj_id() {
        return smestaj_id;
    }

    public void setSmestaj_id(Long smestaj_id) {
        this.smestaj_id = smestaj_id;
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
