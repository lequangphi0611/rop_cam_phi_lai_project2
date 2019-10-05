package com.electronicssales.repositories.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;

import com.electronicssales.repositories.MyCustomizeRepository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class DefaultMyCustomizeRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
        implements MyCustomizeRepository<T, ID> {

    private EntityManager entityManager;

    public DefaultMyCustomizeRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public <S extends T> S persist(S entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public <S extends T> S merge(S entity) {
        return entityManager.merge(entity);
    }

    
}