package com.electronicssales.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.electronicssales.models.ImportInvoiceFetchOption;
import com.electronicssales.models.ImportInvoiceProjections;

public interface CustomizeImportInvoiceRepository {

	Page<ImportInvoiceProjections> getImportInvoices(ImportInvoiceFetchOption option, Pageable pageable);
	
}
