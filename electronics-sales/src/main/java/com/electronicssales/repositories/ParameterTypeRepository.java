package com.electronicssales.repositories;

import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.ParameterType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParameterTypeRepository 
    extends MyCustomizeRepository<ParameterType, Long> {

    String FIND_ALL_QUERY = "SELECT p FROM ParameterType p ORDER BY p.parameterTypeName ASC";

    boolean existsByParameterTypeName(String parameterTypeName);

    Optional<ParameterType> findByParameterTypeName(String parameterTypeName);

    List<ParameterType> findByCategoriesId(long categoryId);

    @Override
    @Query(FIND_ALL_QUERY)
    List<ParameterType> findAll();

    List<ParameterType> findByParameterTypeNameContaining(String name);

}