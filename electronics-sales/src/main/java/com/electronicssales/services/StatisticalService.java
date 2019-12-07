package com.electronicssales.services;

import java.util.List;

import com.electronicssales.models.RevenueOverMonthStatisticalProjections;
import com.electronicssales.models.RevenueProductStatisticalProjections;
import com.electronicssales.models.responses.CategoryStatisticalResponse;

public interface StatisticalService {

	CategoryStatisticalResponse getCategoryStatistical();
	
	List<RevenueProductStatisticalProjections> getRevenueProductStatistical(Integer top);
	
	List<RevenueOverMonthStatisticalProjections> getRevenueOverMonthStatistical();
	
}
