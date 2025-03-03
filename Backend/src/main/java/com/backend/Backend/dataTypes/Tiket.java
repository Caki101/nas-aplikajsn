package com.backend.Backend.dataTypes;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "tiketi")
public class Tiket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_seq")
    @SequenceGenerator(name = "id_seq", sequenceName = "id_seq", allocationSize = 1, initialValue = 0)
    private Integer id;

    private String sezona;
    private String drzava;
    private String grad;

    @ManyToOne
    @JoinColumn(name = "smestaj_id", referencedColumnName = "id")
    private Smestaj smestaj;
    private Double cena;
    private Integer trajanje_odmora;
    private Integer broj_osoba;
    private Integer broj_tiketa;
    private String prevoz;
    private Timestamp polazak;

    @org.springframework.data.annotation.Version
    private Integer version = 0;

    public Tiket() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Smestaj getSmestaj() {
        return smestaj;
    }

    public void setSmestaj(Smestaj smestaj) {
        this.smestaj = smestaj;
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
