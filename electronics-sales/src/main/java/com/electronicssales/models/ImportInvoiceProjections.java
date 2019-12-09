package com.electronicssales.models;

import java.util.Date;

import lombok.ToString;
import lombok.Value;

@Value
public class ImportInvoiceProjections {

	Long id;
	
	Date importTime;
	
	Integer quantity;
	
	String creatorUsername;
	
	String productName;
	
	@ToString.Exclude
	byte[] productImage;
	
	@ToString.Exclude
	byte[] creatorAvartar;
	
}
