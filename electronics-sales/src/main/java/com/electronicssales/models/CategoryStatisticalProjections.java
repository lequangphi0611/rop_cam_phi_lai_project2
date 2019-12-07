package com.electronicssales.models;

import lombok.Value;

@Value
public class CategoryStatisticalProjections {
	
	String categoryName;
	
	Integer productCount;
	
	Integer totalProductSold;
	
}
