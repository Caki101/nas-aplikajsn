package com.backend.Backend.repositories;

import com.backend.Backend.dataTypes.User;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepo extends CrudRepository<User, Long> {
}