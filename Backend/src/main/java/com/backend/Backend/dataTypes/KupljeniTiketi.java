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

    @ManyToOne()
    @JoinColumn(name = "id_tiket", referencedColumnName = "id")
    private Tiket tiket;

    @ManyToOne()
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public KupljeniTiketi() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tiket getTiket() {
        return tiket;
    }

    public void setTiket(Tiket tiket) {
        this.tiket = tiket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
