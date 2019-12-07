package com.electronicssales.models;

import java.util.Date;

import lombok.Builder;
import lombok.Value;

@Value
public class ImportInvoiceFetchOption {

	Date fromDate;
	
	Date toDate;

	@Builder
	private ImportInvoiceFetchOption(Date fromDate, Date toDate) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
	}
	
}
