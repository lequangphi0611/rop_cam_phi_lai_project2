package com.electronicssales.utils;

public enum SqlDelimiters implements EnumCharSequence {

	SPACE(" "),
	
	COMMA(","),
	
	COMMA_SPACE(", ");
	
	private final String value;
	
	private SqlDelimiters(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
}
