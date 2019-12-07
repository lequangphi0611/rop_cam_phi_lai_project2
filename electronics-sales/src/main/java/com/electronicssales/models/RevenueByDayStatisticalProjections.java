package com.electronicssales.models;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RevenueByDayStatisticalProjections extends RevenueByMonthStatisticalProjections {

	Integer day;

	public RevenueByDayStatisticalProjections(Integer day, Integer month, Integer year, Long minRevenue, Long maxRevenue,
			Long totalRevenue) {
		super(month, year, minRevenue, maxRevenue, totalRevenue);
		this.day = day;
	}
	
}
