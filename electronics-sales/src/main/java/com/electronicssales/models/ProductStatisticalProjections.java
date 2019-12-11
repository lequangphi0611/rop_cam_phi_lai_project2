package com.electronicssales.models;

import lombok.ToString;
import lombok.Value;

@Value
public class ProductStatisticalProjections {

	@ToString.Exclude
	private byte[] image;
	
	private Long id;
	
	private String productName;
	
	private Integer quantitySold;
	
	private Long totalDiscount;
	
	private Long totalRevenue;
	
}
