package com.electronicssales.services;

import java.util.List;

import com.electronicssales.models.TransactionDetailedProjections;
import com.electronicssales.models.TransactionFetchOption;
import com.electronicssales.models.TransactionProjections;
import com.electronicssales.models.dtos.TransactionDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    TransactionDto create(TransactionDto transaction);

    List<TransactionDto> findByCustomerInfoId(long customerInfoId);

    List<TransactionDetailedProjections> findTransactionDetailedByTransactionId(long transactionId);

    Page<TransactionProjections> fetchAll(TransactionFetchOption option, Pageable pageable);
    
    Page<TransactionProjections> fetchAll(long customerId, TransactionFetchOption option, Pageable pageable);
}