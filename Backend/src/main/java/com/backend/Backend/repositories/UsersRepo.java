package com.backend.Backend.repositories;

import com.backend.Backend.dataTypes.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsersRepo extends CrudRepository<User, Long> {
    List<User> findByEmail(String email);

    User findFirstByEmail(String email);

    boolean existsUserByEmail(String email);
}