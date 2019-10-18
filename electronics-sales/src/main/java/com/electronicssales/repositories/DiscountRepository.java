package com.electronicssales.repositories;

import com.electronicssales.entities.Discount;

import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository 
    extends MyCustomizeRepository<Discount, Long>,
        CustomizeDiscountRepository {
    
}