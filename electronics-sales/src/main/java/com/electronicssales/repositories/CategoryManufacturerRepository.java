package com.electronicssales.repositories;

import java.util.List;

import com.electronicssales.entities.CategoryManufacturer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryManufacturerRepository extends MyCustomizeRepository<CategoryManufacturer, Long> {

    String FETCH_MANUFACTURERS_BY_CATEGORY_ID_QUERY = "SELECT c " 
        +   " FROM CategoryManufacturer c JOIN FETCH c.manufacturer"
        +   " WHERE c.category.id = ?1";
    
    void deleteByCategoryId(long categoryId);

    @Query(value = FETCH_MANUFACTURERS_BY_CATEGORY_ID_QUERY)
    List<CategoryManufacturer> fetchCategoryManufacturersByCategoryId(long categoryId);
    
}