package com.electronicssales.repositories;

import java.util.List;

import com.electronicssales.models.CategoryStatisticalProjections;
import com.electronicssales.models.RevenueOverMonthStatisticalProjections;
import com.electronicssales.models.RevenueProductStatisticalProjections;

public interface BaseStatisticalRepository {

	List<CategoryStatisticalProjections> getCategoryStatistical();
	
	List<RevenueProductStatisticalProjections> getRevenueProductStatistical(Integer top);

	List<RevenueOverMonthStatisticalProjections> getRevenueMonthStatistical();
	
}
