package com.backend.Backend.dataTypes;

import jakarta.persistence.*;

@Entity
@Table(name = "smestaj")
public class Smestaj {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "smestaj_id_seq")
    @SequenceGenerator(name = "smestaj_id_seq", sequenceName = "smestaj_id_seq", allocationSize = 1, initialValue = 0)
    private Integer id;
    String ime_smestaja;
    String grad;
    Integer ocena;

    public Smestaj() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIme_smestaja() {
        return ime_smestaja;
    }

    public void setIme_smestaja(String ime_smestaja) {
        this.ime_smestaja = ime_smestaja;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public Integer getOcena() {
        return ocena;
    }

    public void setOcena(Integer ocena) {
        this.ocena = ocena;
    }
}
