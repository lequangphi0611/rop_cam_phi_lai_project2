package com.electronicssales.services;

import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.Discount;
import com.electronicssales.models.dtos.DiscountDto;
import com.electronicssales.models.responses.DiscountFullResponse;
import com.electronicssales.models.responses.ProductResponse;

public interface DiscountService {

    Discount saveDiscount(DiscountDto discountDto);

    List<DiscountFullResponse> fetchDiscounts();

    Discount updateDiscount(DiscountDto discountDto);

    void deleteById(long discountId);

    List<ProductResponse> getProducts(long discountId);

    Optional<Discount> findById(long id);
    
}