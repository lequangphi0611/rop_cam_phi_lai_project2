package com.electronicssales.repositories.impl;

import java.util.List;

import javax.persistence.EntityManager;

import com.electronicssales.models.UserInfoProjections;
import com.electronicssales.repositories.CustomizeUserInfoRepository;

public class CustomizeUserInfoRepositoryImpl implements CustomizeUserInfoRepository {

    private static final String FIND_ALL_NATIVE_QUERY = "SELECT * FROM user_infos u";

    private static final String USER_INFO_PROJECTIONS_MAPPING = "UserInfoProjectionsMapping";
    
    private final EntityManager entityManager;

    public CustomizeUserInfoRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UserInfoProjections> findAllUserInfoProjections() {
        return entityManager
            .createNativeQuery(FIND_ALL_NATIVE_QUERY, USER_INFO_PROJECTIONS_MAPPING)
            .getResultList();
    }

    
    
}