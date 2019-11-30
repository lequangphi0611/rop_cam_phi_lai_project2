package com.electronicssales.repositories;

import com.electronicssales.models.DiscountFetchOption;
import com.electronicssales.models.DiscountProjections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomizeDiscountRepository {

    Page<DiscountProjections> fetchDiscounts(DiscountFetchOption option, Pageable pageable);
    
}