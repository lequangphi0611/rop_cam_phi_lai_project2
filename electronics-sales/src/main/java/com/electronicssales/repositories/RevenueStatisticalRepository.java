package com.electronicssales.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.electronicssales.models.RevenueStatisticalProjections;
import com.electronicssales.models.StatisticalType;

public interface RevenueStatisticalRepository {

	<T extends RevenueStatisticalProjections> Page<T> getRevenueStatistical(StatisticalType statisticalType, Pageable pageable);
	
}
