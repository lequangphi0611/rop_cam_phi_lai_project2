package com.electronicssales.repositories;

import com.electronicssales.models.TransactionFetchOption;
import com.electronicssales.models.TransactionProjections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomizeTransactionRepository {

    Page<TransactionProjections> fetchAll(TransactionFetchOption option, Pageable pageable);
    
}