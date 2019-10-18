package com.electronicssales.services;

import java.util.List;

import com.electronicssales.entities.Discount;
import com.electronicssales.models.dtos.DiscountDto;
import com.electronicssales.models.responses.DiscountResponse;

public interface DiscountService {

    Discount saveDiscount(DiscountDto discountDto);

    List<DiscountResponse> fetchDiscountsEnabled();

    Discount updateDiscount(DiscountDto discountDto);

    void deleteById(long discountId);
    
}