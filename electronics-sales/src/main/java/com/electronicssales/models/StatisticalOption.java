package com.electronicssales.models;

import java.util.Date;

import org.springframework.data.domain.Pageable;

import lombok.Data;

@Data
public class StatisticalOption {

	Date fromDate;
	
	Date toDate;
	
	Pageable pageable;
	
	StatisticalType statisticalType;
}
