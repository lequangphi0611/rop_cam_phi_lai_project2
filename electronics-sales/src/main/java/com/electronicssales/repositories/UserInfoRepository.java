package com.electronicssales.repositories;

import java.util.Optional;

import com.electronicssales.entities.UserInfo;
import com.electronicssales.models.UserInfoProjections;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository 
    extends JpaRepository<UserInfo, Long>, CustomizeUserInfoRepository {

    Optional<UserInfoProjections> findByIdOrUserId(Long id, Long userId);

    default Optional<UserInfoProjections> findByIdOrUserId(Long idOrUserId) {
        return findByIdOrUserId(idOrUserId, idOrUserId);
    };

}