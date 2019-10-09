package com.electronicssales.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.ProductCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository 
    extends JpaRepository<ProductCategory, Long>, 
        CustomizeProductCategoryRepository {

    String FIND_BY_PRODUCT_ID_AND_PRODUCT_CATEGORY_IDS_NOT_IN = "SELECT p FROM ProductCategory p" 
        +   " WHERE p.product.id = :productId"
        +   " AND p.id NOT IN (:productCategoryIds)";

    Optional<ProductCategory> findByCategoryIdAndProductId(long categoryId, long productId);

    Collection<ProductCategory> findAllByProductId(long productId);

    @Query(value = FIND_BY_PRODUCT_ID_AND_PRODUCT_CATEGORY_IDS_NOT_IN)
    List<ProductCategory> findByProductCategoryNotIn(
        @Param("productId") long productId, 
        @Param("productCategoryIds") Collection<Long> productCategoryIds
    );

    int countByCategoryId(long categoryId);
    
}