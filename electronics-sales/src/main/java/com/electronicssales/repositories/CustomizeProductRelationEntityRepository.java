package com.electronicssales.repositories;

import java.util.Collection;

import com.electronicssales.entities.Product;

import org.springframework.transaction.annotation.Transactional;

public interface CustomizeProductRelationEntityRepository<T, E> {

    Collection<T> createAll(Product product, Collection<E> entities);

    void deleteByProduct(Product product);

    @Transactional
    default Collection<T> updateAll(Product product, Collection<E> entities) {
        deleteByProduct(product);
        return createAll(product, entities);
    }
    
}