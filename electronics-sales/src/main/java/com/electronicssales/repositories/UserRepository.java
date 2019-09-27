package com.electronicssales.repositories;

import java.util.Optional;

import com.electronicssales.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String FIND_BY_USERNAME_OR_EMAIL_OR_PHONE_NUMBER_QUERY = "SELECT user FROM User user" 
        +   " WHERE user.username = ?1"
        +   " OR user.email = ?1"
        +   " OR user.phoneNumber = ?1";
    
    Optional<User> findByUsername(String username);

    @Query(value = FIND_BY_USERNAME_OR_EMAIL_OR_PHONE_NUMBER_QUERY)
    Optional<User> findByUsernameOrEmailOrPhoneNumber(String usernameOrEmailOrPhoneNumber);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

}