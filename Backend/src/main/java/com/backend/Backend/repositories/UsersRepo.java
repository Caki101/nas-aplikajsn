package com.backend.Backend.repositories;

import com.backend.Backend.dataTypes.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UsersRepo extends CrudRepository<User, Long> {
    User findFirstByEmail(String email);

    User findFirstByUsername(String username);

    boolean existsUserByEmail(String email);

    @Query(nativeQuery = true, value = "UPDATE users SET verified = true WHERE email = ?1")
    @Modifying
    @Transactional
    void verified(@Param("email") String email);

    @Query(nativeQuery = true, value = "select * from insert_admin(?1)")
    boolean promote(@Param("user_id") Long user_id);

    @Query(nativeQuery = true, value = "select exists (select 1 from admins where user_id = ?1)")
    Boolean checkAdmin(@Param("user_id") Long user_id);
}