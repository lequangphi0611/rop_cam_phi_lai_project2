package com.electronicssales.utils;

public enum SqlKeyWords implements EnumCharSequence {

	SELECT("SELECT"),
	
	FROM("FROM"),
	
	WHERE("WHERE"),
	
	GROUP_BY("GROUP BY"),
	
	HAVING("HAVING"),
	
	ORDER_BY("ORDER BY");
	
	
	private final String value;
	
	private SqlKeyWords(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
