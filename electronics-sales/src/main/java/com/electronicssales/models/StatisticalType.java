package com.electronicssales.models;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum StatisticalType {

	DAY,
	
	MONTH,
	
	YEAR;
	
	public static Optional<StatisticalType> of(String args) {
		if(Objects.isNull(args)) {
			return Optional.empty();
		}
		
		return Arrays.stream(StatisticalType.values())
				.filter(value -> value.toString().toLowerCase().equals(args.toLowerCase()))
				.findFirst();
	}
	
}
