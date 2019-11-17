package com.electronicssales.repositories;

import com.electronicssales.entities.ProductCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    String COUNT_PRODUCT_BY_CATEGORY_ID = "SELECT COUNT(pc.id) FROM ProductCategory pc JOIN pc.category c"
        + " JOIN pc.product p WHERE c.id = ?1";
    
    @Query(COUNT_PRODUCT_BY_CATEGORY_ID)
    int countByCategoryId(long categoryId);
}