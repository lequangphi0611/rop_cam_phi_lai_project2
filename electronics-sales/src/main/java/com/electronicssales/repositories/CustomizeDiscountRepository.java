package com.electronicssales.repositories;

import com.electronicssales.entities.Discount;
import com.electronicssales.models.DiscountFetchOption;
import com.electronicssales.models.DiscountProjections;
import com.electronicssales.models.dtos.DiscountDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomizeDiscountRepository {

    Page<DiscountProjections> fetchDiscounts(DiscountFetchOption option, Pageable pageable);

    Discount create(DiscountDto discountDto);

    Discount update(DiscountDto discountDto);

    void customizeDeleteById(Long id);
    
}