package com.backend.Backend.dataTypes;

import jakarta.persistence.*;

@Entity
@Table(name = "smestaj")
public class Smestaj {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "smestaj_id_seq")
    @SequenceGenerator(name = "smestaj_id_seq", sequenceName = "smestaj_id_seq", allocationSize = 1, initialValue = 0)
    private Long id;

    String ime_smestaja;
    String drzava;
    String grad;
    Double ocena;

    public Smestaj() {
    }

    @Override
    public String toString() {
        return "Smestaj{" +
                "id=" + id +
                ", ime_smestaja='" + ime_smestaja + '\'' +
                ", drzava='" + drzava + '\'' +
                ", grad='" + grad + '\'' +
                ", ocena=" + ocena +
                '}';
    }

    public String searchable() {
        return (id + " " +
                ime_smestaja + " " +
                drzava + " " +
                grad + " " +
                ocena).toLowerCase();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme_smestaja() {
        return ime_smestaja;
    }

    public void setIme_smestaja(String ime_smestaja) {
        this.ime_smestaja = ime_smestaja;
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

    public Double getOcena() {
        return ocena;
    }

    public void setOcena(Double ocena) {
        this.ocena = ocena;
    }
}
