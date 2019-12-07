package com.electronicssales.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.electronicssales.models.CategoryStatisticalProjections;
import com.electronicssales.models.RevenueOverMonthStatisticalProjections;
import com.electronicssales.models.RevenueProductStatisticalProjections;

@Repository
public interface StatisticalRepository {

	List<CategoryStatisticalProjections> getCategoryStatistical();
	
	List<RevenueProductStatisticalProjections> getRevenueProductStatistical(Integer top);

	List<RevenueOverMonthStatisticalProjections> getRevenueMonthStatistical();
	
}
