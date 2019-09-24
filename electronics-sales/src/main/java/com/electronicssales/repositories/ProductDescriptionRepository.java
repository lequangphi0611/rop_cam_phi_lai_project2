package com.electronicssales.repositories;

import com.electronicssales.entities.ProductDescription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDescriptionRepository extends JpaRepository<ProductDescription, Long> {

    
}