package com.electronicssales.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.electronicssales.models.ImportInvoiceFetchOption;
import com.electronicssales.models.ImportInvoiceProjections;

public interface CustomizeImportInvoiceRepository {

	List<ImportInvoiceProjections> getImportInvoices(ImportInvoiceFetchOption option, Pageable pageable);
	
}
