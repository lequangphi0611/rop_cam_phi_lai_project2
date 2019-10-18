package com.electronicssales.repositories;

import com.electronicssales.entities.Discount;

public interface CustomizeDiscountRepository {

    Discount updateDiscount(Discount discount);
    
}