package com.electronicssales.resources;

import java.util.Date;
import java.util.concurrent.Callable;

import com.electronicssales.models.TransactionFetchOption;
import com.electronicssales.models.dtos.TransactionDto;
import com.electronicssales.services.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionResource {

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    @Lazy
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public Callable<ResponseEntity<?>> fetchAll(
        Pageable pageable,
        @RequestParam(value = "fromDate", required = false) 
        @DateTimeFormat(pattern = DATE_FORMAT_PATTERN) 
        Date fromDate,
        @RequestParam(value = "toDate", required = false) 
        @DateTimeFormat(pattern = DATE_FORMAT_PATTERN) 
        Date toDate
    ) {
        TransactionFetchOption option = new TransactionFetchOption();
        option.setFromDate(fromDate);
        option.setToDate(toDate);
        return () -> ResponseEntity.ok(this.transactionService.fetchAll(option, pageable));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TransactionDto transactionRequest) {
        return ResponseEntity.created(null).body(transactionService.create(transactionRequest));
    }

    @GetMapping("/{id}/transaction-detaileds")
    public ResponseEntity<?> fetchTransactionDetailed(@PathVariable("id") long transactionId) {
        return ResponseEntity
                .ok(transactionService.findTransactionDetailedByTransactionId(transactionId));
    }

}