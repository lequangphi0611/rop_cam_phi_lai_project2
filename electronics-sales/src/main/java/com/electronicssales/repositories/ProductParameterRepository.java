package com.electronicssales.repositories;

import java.util.Collection;

import com.electronicssales.entities.ProductParameter;
import com.electronicssales.models.responses.ProductParameterRepositoryResponse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductParameterRepository 
    extends JpaRepository<ProductParameter, Long>,
    CustomizeProductParameterRepository {

    String PRODUCT_PARAMETER_NATIVE_QUERY_BY_PRODUCT_ID = "SELECT p.id as productParameterId,"
        +   " t.parameter_type_name AS parameterType,"
        +   " p.parameter_value AS parameterValue"
        +   " FROM product_parameters p"
        +   " INNER JOIN parameter_types t"
        +   " ON p.parameter_type_id = t.id" 
        +   " WHERE p.product_id = ?1" 
        +   " ORDER BY t.id ASC";

    @Query(value = PRODUCT_PARAMETER_NATIVE_QUERY_BY_PRODUCT_ID, nativeQuery = true)
    Collection<ProductParameterRepositoryResponse> fetchByProductId(long id);
    
}