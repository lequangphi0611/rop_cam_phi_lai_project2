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

    String UPDATE_PRODUCT_QUANTITY_QUERY = "UPDATE Product p"
        +   " SET p.quantity = p.quantity + :newQuantity"
        +   " WHERE p.id = :productId";

    String REMOVE_ALL_DISCOUNT_QUERY = "UPDATE products"
        +   " SET discount_id = null WHERE discount_id = ?1";

    String FIND_BY_ID_QUERY = "SELECT p FROM Product p LEFT JOIN FETCH p.discount d WHERE p.id = ?1";

    Optional<Product> findByProductName(String productName);

    boolean existsByProductName(String productName);

    @Modifying
    @Query(value = UPDATE_PRODUCT_QUANTITY_QUERY)
    void updateProductQuantity( @Param("newQuantity") int newQuantity,
                                @Param("productId") long productId);

    @Modifying
    @Query(value = REMOVE_ALL_DISCOUNT_QUERY, nativeQuery = true)
    void removeAllDiscount(long discountId);

    @Override
    @Query(value = FIND_BY_ID_QUERY)
    Optional<Product> findById(Long id);
}