package com.electronicssales.repositories;

import java.util.List;

import com.electronicssales.models.UserInfoProjections;

public interface CustomizeUserInfoRepository {

    List<UserInfoProjections> findAllUserInfoProjections();
    
}