package com.electronicssales.services;

import java.util.List;
import java.util.Optional;

import com.electronicssales.models.UserInfoProjections;
import com.electronicssales.models.responses.RequiredUserInfoDto;

public interface UserInfoService {

    List<UserInfoProjections> findAll();

    Optional<UserInfoProjections> findById(Long id);

    Optional<UserInfoProjections> findByIdOrUserId(Long idOrUserId);

    UserInfoProjections save(RequiredUserInfoDto userInfo);
    
    void deleteById(Long id);
}