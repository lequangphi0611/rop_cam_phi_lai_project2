package com.electronicssales.repositories;

import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.CategoryParameterType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryParameterTypeRepository extends JpaRepository<CategoryParameterType, Long> {
    
    String FIND_ALL_BY_CATEGORY_ID = "SELECT c FROM CategoryParameterType c"
        +   " JOIN FETCH c.parameterType WHERE c.category.id = ?1";

    Optional<CategoryParameterType> findByCategoryIdAndParameterTypeId(long categoryId, long parameterId);

    @Query(value = FIND_ALL_BY_CATEGORY_ID)
    List<CategoryParameterType> findAllByCategoryId(long category);

}