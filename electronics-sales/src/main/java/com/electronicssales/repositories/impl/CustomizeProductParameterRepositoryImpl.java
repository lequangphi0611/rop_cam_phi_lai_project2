package com.electronicssales.repositories.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.electronicssales.entities.Product;
import com.electronicssales.entities.ProductParameter;
import com.electronicssales.repositories.CustomizeProductParameterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CustomizeProductParameterRepositoryImpl implements CustomizeProductParameterRepository {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    @Override
    public Collection<ProductParameter> createAll(Product product, Collection<ProductParameter> productParameters) {
        return productParameters
            .stream()
            .peek(productParameter -> productParameter.setProduct(product))
            .map(productParameter -> {
                entityManager.persist(productParameter);
                return productParameter;
            })
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteByProduct(Product product) {
        String sqlQuery = new StringBuilder("DELETE FROM ")
            .append(ProductParameter.class.getSimpleName())
            .append(" p WHERE p.product.id = :productId")
            .toString();

        entityManager
            .createQuery(sqlQuery)
            .setParameter("productId", product.getId())
            .executeUpdate();
    }

    
}