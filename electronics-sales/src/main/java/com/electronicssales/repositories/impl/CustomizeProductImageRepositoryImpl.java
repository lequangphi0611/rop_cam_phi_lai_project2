package com.electronicssales.repositories.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.electronicssales.entities.Image;
import com.electronicssales.entities.Product;
import com.electronicssales.entities.ProductImage;
import com.electronicssales.repositories.CustomizeProductImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CustomizeProductImageRepositoryImpl implements CustomizeProductImageRepository {

    @Autowired
    private EntityManager entityManager;

    private ProductImage getProductImageFrom(Product product, Image image) {
        ProductImage productImage = new ProductImage();
        productImage.setImage(image);
        productImage.setProduct(product);
        return productImage;
    }

    @Transactional
    @Override
    public Collection<ProductImage> createAll(Product product, Collection<Image> images) {
        return images 
            .stream()
            .map((image) -> getProductImageFrom(product, image))
            .map((productImage) -> {
                entityManager.persist(productImage);
                return productImage;
            })
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteByProduct(Product product) {
        StringBuilder sqlQueryStr = new StringBuilder("DELETE FROM ");
        sqlQueryStr
            .append(ProductImage.class.getSimpleName())
            .append(" p WHERE p.product.id = :productId");

        entityManager
            .createQuery(sqlQueryStr.toString())
            .setParameter("productId", product.getId())
            .executeUpdate();

    }

    
}