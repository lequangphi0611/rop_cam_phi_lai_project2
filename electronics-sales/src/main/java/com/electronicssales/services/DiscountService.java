package com.electronicssales.services;

import java.util.List;
import java.util.Optional;

import com.electronicssales.entities.Discount;
import com.electronicssales.models.DiscountFetchOption;
import com.electronicssales.models.DiscountProjections;
import com.electronicssales.models.dtos.DiscountDto;
import com.electronicssales.models.responses.DiscountFullResponse;
import com.electronicssales.models.responses.ProductResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiscountService {

    Discount saveDiscount(DiscountDto discountDto);

    List<DiscountFullResponse> fetchDiscounts();

    Discount updateDiscount(DiscountDto discountDto);

    void deleteById(long discountId);

    List<ProductResponse> getProducts(long discountId);

    Optional<Discount> findById(long id);

    Page<DiscountProjections> fetchAll(DiscountFetchOption option, Pageable pageable);
    
}