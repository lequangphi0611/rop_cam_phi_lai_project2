package com.electronicssales.models;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NonFinal
public class RevenueByMonthStatisticalProjections extends RevenueStatisticalProjections {
	
	Integer month;
	
	public RevenueByMonthStatisticalProjections(Integer month, Integer year, Long minRevenue, Long maxRevenue, 
			Long totalRevenue) {
		super(year, minRevenue, maxRevenue, totalRevenue);
		this.month = month;
	}
	
}
