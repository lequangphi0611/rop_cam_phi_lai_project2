package com.electronicssales.repositories;

import java.util.Optional;

import com.electronicssales.entities.Product;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository 
    extends MyCustomizeRepository<Product, Long>,
        CustomizeProductRepository {

    String UPDATE_PRODUCT_QUANTITY_QUERY = "UPDATE Product p SET p.quantity = :quantity WHERE p.id = :productId";

    Optional<Product> findByProductName(String productName);

    boolean existsByProductName(String productName);

    @Modifying
    @Query(value = UPDATE_PRODUCT_QUANTITY_QUERY)
    void updateProductQuantity( @Param("quantity") int quantity,
                                @Param("productId") long productId);
    
}