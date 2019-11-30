package com.electronicssales.repositories;

import java.util.List;

import com.electronicssales.entities.Discount;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository 
    extends MyCustomizeRepository<Discount, Long>, CustomizeDiscountRepository {

    String FIND_ALL_QUERY = "SELECT d FROM Discount d ORDER BY d.startedTime DESC";
    
    @Override
    @Query(FIND_ALL_QUERY)
    List<Discount> findAll();

}