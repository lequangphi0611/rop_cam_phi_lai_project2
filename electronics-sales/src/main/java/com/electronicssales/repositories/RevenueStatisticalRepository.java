package com.electronicssales.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.electronicssales.models.RevenueStatisticalProjections;
import com.electronicssales.models.StatisticalType;

public interface RevenueStatisticalRepository {

	<T extends RevenueStatisticalProjections> List<T> getRevenueStatistical(StatisticalType statisticalType, Pageable pageable);
	
}
