package com.electronicssales.repositories.impl;

import javax.persistence.EntityManager;

import com.electronicssales.entities.Category;
import com.electronicssales.entities.ParameterType;
import com.electronicssales.repositories.CustomizeCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Lazy
@Component
public class CustomizeCategoryRepositoryImpl implements CustomizeCategoryRepository {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    @Override
    public void saveCategoryParameter(Category category, ParameterType parameterType) {
        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO categories_parameter_types")
            .append(" (category_id, parameter_type_id) values (?, ?)");

        entityManager
            .createNativeQuery(sqlBuilder.toString())
            .setParameter(1, category.getId())
            .setParameter(2, parameterType.getId())
            .executeUpdate();

    }

    
}