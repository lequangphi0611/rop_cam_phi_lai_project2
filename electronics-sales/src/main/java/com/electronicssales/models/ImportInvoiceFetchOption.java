package com.electronicssales.models;

import java.util.Date;
import java.util.Objects;

import lombok.Builder;
import lombok.Value;

@Value
public class ImportInvoiceFetchOption {
	
	private static final ImportInvoiceFetchOption EMPTY = ImportInvoiceFetchOption.builder().build();

	Date fromDate;
	
	Date toDate;

	@Builder
	private ImportInvoiceFetchOption(Date fromDate, Date toDate) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
	}
	
	public boolean isEmpty() {
		return Objects.isNull(fromDate)
				&& Objects.isNull(toDate);
	}
	
	public static ImportInvoiceFetchOption nonOption() {
		return EMPTY;
	}
	
}
