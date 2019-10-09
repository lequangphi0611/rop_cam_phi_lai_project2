package com.electronicssales.repositories;

import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.ParameterType;

import org.springframework.stereotype.Repository;

@Repository
public interface ParameterTypeRepository 
    extends MyCustomizeRepository<ParameterType, Long> {

    boolean existsByParameterTypeName(String parameterTypeName);

    Optional<ParameterType> findByParameterTypeName(String parameterTypeName);

    List<ParameterType> findByCategoriesId(long categoryId);

}