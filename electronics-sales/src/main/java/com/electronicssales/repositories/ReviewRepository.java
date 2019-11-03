package com.electronicssales.repositories;

import java.util.List;

import com.electronicssales.entities.Review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    String FIND_BY_PRODUCT_ID = "SELECT r FROM Review r"
        +   " JOIN FETCH r.comment c JOIN FETCH c.paragraph"
        +   " WHERE r.product.id = :productId"
        +   " ORDER BY c.createdTime DESC";

    @Query(value = FIND_BY_PRODUCT_ID)
    List<Review> findByProductId(@Param("productId") long productId, Pageable pageable);

}