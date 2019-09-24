package com.electronicssales.repositories;

import java.util.Optional;

import com.electronicssales.entities.CategoryParameterType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryParameterTypeRepository extends JpaRepository<CategoryParameterType, Long> {
    
    Optional<CategoryParameterType> findByCategoryIdAndParameterTypeId(long categoryId, long parameterId);

}