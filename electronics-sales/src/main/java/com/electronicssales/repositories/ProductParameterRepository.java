package com.electronicssales.repositories;

import java.util.Collection;

import com.electronicssales.entities.ProductParameter;
import com.electronicssales.models.responses.ProductParameterResponsitoryResponse;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductParameterRepository extends JpaRepository<ProductParameter, Long> {

    String PRODUCT_PARAMETER_QUERY = "SELECT t.parameter_type_name as parameterType, "
        +   " p.parameter_value as parameterValue "
        +   " FROM product_parameters p INNER JOIN parameter_types t "
        +   " ON p.parameter_type_id = t.id WHERE p.product_id = ?1 " 
        +   " ORDER BY t.id ASC";

    @Query(
        value = PRODUCT_PARAMETER_QUERY,
        nativeQuery = true
    )
    Collection<ProductParameterResponsitoryResponse> fetchByProductId(long id); 
    
}