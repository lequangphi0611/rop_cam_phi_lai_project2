package com.electronicssales.resources;

import javax.validation.Valid;

import com.electronicssales.models.dtos.ImportInvoiceDto;
import com.electronicssales.services.ImportInvoiceService;
import com.electronicssales.services.UserService;
import com.electronicssales.utils.AuthenticateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/import-invoices")
public class ImportInvoiceResource {

    @Lazy
    @Autowired
    private ImportInvoiceService importInvoiceService;

    @Lazy
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createImportInvoice(@RequestBody @Valid ImportInvoiceDto importInvoiceDtoReq) {
        long userId = AuthenticateUtils.getUserPrincipal().getId();
        importInvoiceDtoReq.setUserId(userId);
        return ResponseEntity
            .created(null)
            .body(importInvoiceService.saveImportInvoice(importInvoiceDtoReq));
    }
    
}