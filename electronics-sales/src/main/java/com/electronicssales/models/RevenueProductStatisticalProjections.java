package com.electronicssales.models;

import lombok.Value;

@Value
public class RevenueProductStatisticalProjections {

	String productName;
	
	byte[] image;
	
	Integer numberOfSales;
	
	Integer quantityProductSold;
	
	Long revenue;
	
}
