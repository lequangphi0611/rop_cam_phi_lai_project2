package com.electronicssales.repositories.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.electronicssales.entities.Category;
import com.electronicssales.entities.Product;
import com.electronicssales.entities.ProductCategory;
import com.electronicssales.repositories.CustomizeProductCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CustomizeProductCategoryRepositoryImpl implements CustomizeProductCategoryRepository {

    @Autowired
    private EntityManager entityManager;

    private ProductCategory getProductCategoryFrom(Product product, Category category) {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategory(category);
        productCategory.setProduct(product);
        return productCategory;
    }

    @Transactional
    @Override
    public Collection<ProductCategory> createAll(Product product, Collection<Category> categories) {
        return categories
            .stream()
            .map((category) -> getProductCategoryFrom(product, category))
            .map((productCategory) -> {
                entityManager.persist(productCategory);
                return productCategory;
            })
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteByProduct(Product product) {
        String sqlQuery = new StringBuilder("DELETE FROM ")
            .append(ProductCategory.class.getName())
            .append(" p WHERE p.product.id = :productId")
            .toString(); 

        entityManager
            .createQuery(sqlQuery)
            .setParameter("productId", product.getId())
            .executeUpdate();
    }

    
}