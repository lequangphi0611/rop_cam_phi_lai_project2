package com.electronicssales.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.electronicssales.models.RevenueOverMonthStatisticalProjections;
import com.electronicssales.models.RevenueProductStatisticalProjections;
import com.electronicssales.models.RevenueStatisticalProjections;
import com.electronicssales.models.StatisticalType;
import com.electronicssales.models.responses.CategoryStatisticalResponse;

public interface StatisticalService {

	CategoryStatisticalResponse getCategoryStatistical();
	
	List<RevenueProductStatisticalProjections> getRevenueProductStatistical(Integer top);
	
	List<RevenueOverMonthStatisticalProjections> getRevenueOverMonthStatistical();
	
	<T extends RevenueStatisticalProjections> Page<T> getRevenueStatistical(StatisticalType statisticalType, Pageable pageable);
	
}
