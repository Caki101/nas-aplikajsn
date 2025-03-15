package com.backend.Backend.dataTypes;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "kupljeni_tiketi")
public class KupljeniTiketi {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kt_id_seq")
    @SequenceGenerator(name = "kt_id_seq", sequenceName = "kt_id_seq", allocationSize = 1, initialValue = 0)
    private Long id;

    private Long id_tiket;
    private Long id_user;
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public KupljeniTiketi() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_tiket() {
        return id_tiket;
    }

    public void setId_tiket(Long id_tiket) {
        this.id_tiket = id_tiket;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
