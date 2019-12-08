package com.electronicssales.utils;

public enum SqlOperators implements EnumCharSequence {

	EQUALS(" = "),
	
	LESS_THAN(" < "),
	
	GREATER_THAN(" > "),
	
	GREATER_THAN_OR_EQUAL_TO(" >= "),
	
	LESS_THAN_OR_EQUAL_TO(" <= "),
	
	NOT_EQUAL_TO(" <> "),
	
	NOT_LESS_THAN(" !< "),
	
	NOT_GREATER_THAN(" !> "),
	
	AND(" AND "),
	
	OR(" OR "),
	
	BETWEEN(" BETWEEN "),
	
	IN(" IN "),
	
	LIKE(" LIKE "),
	
	NOT(" NOT ")
	
	;
	
	private final String value;
	
	private SqlOperators(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
