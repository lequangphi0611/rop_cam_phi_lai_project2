package com.electronicssales.resources;

import com.electronicssales.models.dtos.TransactionDto;
import com.electronicssales.services.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionResource {

    @Lazy
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TransactionDto transactionRequest) {
        return ResponseEntity.created(null).body(transactionService.create(transactionRequest));
    }
    
}