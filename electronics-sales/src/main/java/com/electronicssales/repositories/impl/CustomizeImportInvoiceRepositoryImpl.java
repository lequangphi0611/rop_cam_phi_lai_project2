package com.electronicssales.repositories.impl;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Pageable;

import com.electronicssales.models.ImportInvoiceFetchOption;
import com.electronicssales.models.ImportInvoiceProjections;
import com.electronicssales.repositories.CustomizeImportInvoiceRepository;

public class CustomizeImportInvoiceRepositoryImpl implements CustomizeImportInvoiceRepository {
	
	private static final String FETCH_ALL_IMPORT_INVOICE_QUERY = "SELECT " + 
			"	import.id as id," + 
			"	import.import_time as importTime," + 
			"	import.quantity as quantity," + 
			"	u.username as creatorUsername," + 
			"	p.product_name as productName, " + 
			"	(SELECT TOP 1" + 
			"		i.data  " + 
			"	FROM images i" + 
			"		INNER JOIN product_images pImage" + 
			"			ON i.id = pImage.image_id" + 
			"	WHERE pImage.product_id = p.id) as productImage" + 
			"	FROM import_invoices import" + 
			"		INNER JOIN users u" + 
			"			ON import.creator_id = u.id" + 
			"		INNER JOIN products p" + 
			"			ON import.product_id = p.id";

	@Override
	public List<ImportInvoiceProjections> getImportInvoices(ImportInvoiceFetchOption option, Pageable pageable) {
		StringBuilder builder = new StringBuilder(FETCH_ALL_IMPORT_INVOICE_QUERY);
		return null;
	}
	
	@Bean
	public void test() {
		
	}

}
