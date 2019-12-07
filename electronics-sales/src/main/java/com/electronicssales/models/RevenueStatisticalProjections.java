package com.electronicssales.models;

import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class RevenueStatisticalProjections {

	Integer year;
	
	Long minRevenue;
	
	Long maxRevenue;
	
	Long totalRevenue;
	
}
