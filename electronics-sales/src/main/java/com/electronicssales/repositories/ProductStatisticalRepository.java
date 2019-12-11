package com.electronicssales.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.electronicssales.models.ProductStatisticalProjections;

public interface ProductStatisticalRepository {

	Page<ProductStatisticalProjections> getProductStatistical(Pageable pageable);
	
}
