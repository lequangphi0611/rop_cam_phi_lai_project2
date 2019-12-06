package com.electronicssales.repositories;

import java.util.Optional;

import com.electronicssales.entities.User;
import com.electronicssales.models.UserInfoIDOnly;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MyCustomizeRepository<User, Long>, CustomizeUserRepository {

    String FIND_BY_USERNAME_AND_FETCH_USERINFO_QUERY = "SELECT u FROM User u JOIN FETCH u.userInfo WHERE u.username = ?1";

    String FIND_BY_ID_AND_FETCH_USERINFO_QUERY = "SELECT u FROM User u JOIN FETCH u.userInfo WHERE u.id = ?1";

    String FIND_BY_ID = "SELECT u FROM User u WHERE u.id = ?1";

    String UPDATE_ATIVED = "UPDATE users SET actived = ?2 WHERE id = ?1";

    String UPDATE_PASSWORD_BY_USERNAME = "UPDATE users SET password = ?2 WHERE username = ?1";

    String GET_PASSWORD_BY_USERNAME = "SELECT u.password FROM User u WHERE u.username = ?1";
    
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(FIND_BY_USERNAME_AND_FETCH_USERINFO_QUERY)
    Optional<User> findByUsernameAndFetchUserInfo(String username);

    @Query(FIND_BY_ID_AND_FETCH_USERINFO_QUERY)
    Optional<User> findByIdAndFetchUserInfo(Long id);

    @Query(FIND_BY_ID)
    UserInfoIDOnly findUserInfoById(long userId);

    @Modifying
    @Query(value = UPDATE_ATIVED, nativeQuery = true)
    void updateActived(long userId, boolean actived);

    @Modifying
    @Query(value = UPDATE_PASSWORD_BY_USERNAME, nativeQuery = true)
    void updatePasswordByUserName(String username, String newPassword);

    @Query(GET_PASSWORD_BY_USERNAME)
    Optional<String> getPasswordOf(String username);
}