package com.electronicssales.repositories;

import java.util.Optional;

import com.electronicssales.entities.User;
import com.electronicssales.models.UserInfoIDOnly;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MyCustomizeRepository<User, Long> {

    String FIND_BY_USERNAME_AND_FETCH_USERINFO_QUERY = "SELECT u FROM User u JOIN FETCH u.userInfo WHERE u.username = ?1";

    String FIND_BY_ID_AND_FETCH_USERINFO_QUERY = "SELECT u FROM User u JOIN FETCH u.userInfo WHERE u.id = ?1";

    String FIND_BY_ID = "SELECT u FROM User u WHERE u.id = ?1";
    
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(FIND_BY_USERNAME_AND_FETCH_USERINFO_QUERY)
    Optional<User> findByUsernameAndFetchUserInfo(String username);

    @Query(FIND_BY_ID_AND_FETCH_USERINFO_QUERY)
    Optional<User> findByIdAndFetchUserInfo(Long id);

    @Query(FIND_BY_ID)
    UserInfoIDOnly findUserInfoById(long userId);

}