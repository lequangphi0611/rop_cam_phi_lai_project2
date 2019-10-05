package com.electronicssales.repositories;

import java.util.Optional;

import com.electronicssales.entities.CategoryManufacturer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryManufacturerRepository extends JpaRepository<CategoryManufacturer, Long> {

    Optional<CategoryManufacturer> findByCategoryIdAndManufacturerId(long categoryId, long manufacturerId);
    
}