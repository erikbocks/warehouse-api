package com.bock.warehouseapi.repositories;

import com.bock.warehouseapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u from users u WHERE lower(u.username) = lower(:username)")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u from users u WHERE lower(u.email) = lower(:email)")
    User findByEmail(@Param("email") String email);

    @Query("SELECT u FROM users u WHERE lower(u.username) = lower(:login) OR lower(u.email) = lower(:login)")
    UserDetails findBySubject(@Param("login") String login);
}
