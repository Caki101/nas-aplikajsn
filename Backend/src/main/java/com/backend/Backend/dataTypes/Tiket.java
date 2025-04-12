package com.backend.Backend.dataTypes;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "tiketi")
public class Tiket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tiketi_id_seq")
    @SequenceGenerator(name = "tiketi_id_seq", sequenceName = "tiketi_id_seq", allocationSize = 1, initialValue = 0)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "smestaj_id", referencedColumnName = "id")
    private Smestaj smestaj;

    private String sezona;
    private Double cena;
    private Integer trajanje_odmora;
    private Integer broj_osoba;
    private Integer broj_tiketa;
    private String prevoz;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss", timezone = "UTC")
    private Timestamp polazak;

    @org.springframework.data.annotation.Version
    private Integer version = 0;

    public Tiket() {
    }

    public Tiket(TiketDDSO tiketDDSO) {
        this.smestaj = tiketDDSO.getSmestaj();
        this.sezona = tiketDDSO.getSezona();
        this.cena = tiketDDSO.getCena();
        this.trajanje_odmora = tiketDDSO.getTrajanje_odmora();
        this.broj_osoba = tiketDDSO.getBroj_osoba();
        this.broj_tiketa = tiketDDSO.getBroj_tiketa();
        this.prevoz = tiketDDSO.getPrevoz();
        this.polazak = tiketDDSO.getPolazak();
    }

    @Override
    public String toString() {
        return "Tiket{" +
                "id=" + id +
                ", smestaj=" + smestaj.toString() +
                ", sezona='" + sezona + '\'' +
                ", cena=" + cena +
                ", trajanje_odmora=" + trajanje_odmora +
                ", broj_osoba=" + broj_osoba +
                ", broj_tiketa=" + broj_tiketa +
                ", prevoz='" + prevoz + '\'' +
                ", polazak=" + polazak +
                ", version=" + version +
                '}';
    }

    public String searchable() {
        return (id + " " +
                smestaj.searchable() + " " +
                sezona + " " +
                cena + " " +
                trajanje_odmora + " " +
                broj_osoba + " " +
                broj_tiketa + " " +
                prevoz + " " +
                polazak).toLowerCase();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
