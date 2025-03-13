package com.backend.Backend.repositories;

import com.backend.Backend.dataTypes.NewsletterSubscriber;
import org.springframework.data.repository.CrudRepository;

public interface NewsletterRepo extends CrudRepository<NewsletterSubscriber, Long> {
    boolean existsByEmail(String email);
}
