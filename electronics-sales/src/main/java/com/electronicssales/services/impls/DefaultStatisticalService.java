package com.electronicssales.services.impls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.electronicssales.models.RevenueOverMonthStatisticalProjections;
import com.electronicssales.models.RevenueProductStatisticalProjections;
import com.electronicssales.models.RevenueStatisticalProjections;
import com.electronicssales.models.StatisticalType;
import com.electronicssales.models.responses.CategoryStatisticalResponse;
import com.electronicssales.repositories.StatisticalRepository;
import com.electronicssales.services.StatisticalService;

@Lazy
@Service
public class DefaultStatisticalService implements StatisticalService {
	
	@Lazy
	@Autowired 
	private StatisticalRepository statisticalRepository; 
	
	@Override
	public CategoryStatisticalResponse getCategoryStatistical() {
		return new CategoryStatisticalResponse(statisticalRepository.getCategoryStatistical());
	}
	
	@Override
	public List<RevenueProductStatisticalProjections> getRevenueProductStatistical(Integer top) {
		return statisticalRepository.getRevenueProductStatistical(top);
	}
	
	@Override
	public List<RevenueOverMonthStatisticalProjections> getRevenueOverMonthStatistical() {
		return statisticalRepository.getRevenueMonthStatistical();
	}
	
	@Override
	public <T extends RevenueStatisticalProjections> Page<T> getRevenueStatistical(StatisticalType statisticalType,
			Pageable pageable) {
		return statisticalRepository.getRevenueStatistical(statisticalType, pageable);
	}
	
}
