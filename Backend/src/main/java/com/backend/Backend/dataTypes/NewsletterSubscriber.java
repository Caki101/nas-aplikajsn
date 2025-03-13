package com.backend.Backend.dataTypes;

import jakarta.persistence.*;

@Entity
@Table(name = "newsletter")
public class NewsletterSubscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "newsletter_id_seq")
    @SequenceGenerator(name = "newsletter_id_seq", sequenceName = "newsletter_id_seq", allocationSize = 1, initialValue = 0)
    private Long id;

    private String email;

    public NewsletterSubscriber() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
