package com.electronicssales.resources;

import javax.validation.Valid;

import com.electronicssales.models.dtos.ImportInvoiceDto;
import com.electronicssales.services.ImportInvoiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import-invoices")
public class ImportInvoiceResource {

    @Autowired
    private ImportInvoiceService importInvoiceService;

    @PostMapping
    public ResponseEntity<?> createImportInvoice(@RequestBody @Valid ImportInvoiceDto importInvoiceDtoReq) {
        return ResponseEntity
            .created(null)
            .body(importInvoiceService.saveImportInvoice(importInvoiceDtoReq));
    }
    
}