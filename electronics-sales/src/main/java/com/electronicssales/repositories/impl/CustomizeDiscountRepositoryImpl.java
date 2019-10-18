package com.electronicssales.repositories.impl;

import javax.persistence.EntityManager;

import com.electronicssales.entities.Discount;
import com.electronicssales.repositories.CustomizeDiscountRepository;

import org.springframework.beans.factory.annotation.Autowired;

public class CustomizeDiscountRepositoryImpl implements CustomizeDiscountRepository {

    // private static final String UPDATE_DISCOUNT_QUERY = "UPDATE discounts" 
    //     +   " SET discount_type = ?, discount_value = ?, started_time = ? WHERE id = ?";

    @Autowired
    private EntityManager entityManager;

    @Override
    public Discount updateDiscount(Discount discount) {
        return null;
    }

    // public Discount
    
}