package com.electronicssales.services;

import com.electronicssales.entities.ImportInvoice;
import com.electronicssales.models.dtos.ImportInvoiceDto;

public interface ImportInvoiceService {

    ImportInvoice saveImportInvoice(ImportInvoiceDto importInvoice);
    
}