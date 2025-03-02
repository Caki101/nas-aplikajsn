package com.backend.Backend.dataTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TiketDTO {

    private String sezona;
    private String drzava;
    private String grad;

    @JsonProperty("smestaj_id")
    private Integer smestaj_id;
    private Double cena;
    private Integer trajanje_odmora;
    private Integer broj_osoba;
    private Integer broj_tiketa;
    private String prevoz;

    public String getSezona() {
        return sezona;
    }

    public void setSezona(String sezona) {
        this.sezona = sezona;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public Integer getSmestaj_id() {
        return smestaj_id;
    }

    public void setSmestaj_id(Integer smestaj_id) {
        this.smestaj_id = smestaj_id;
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
}
