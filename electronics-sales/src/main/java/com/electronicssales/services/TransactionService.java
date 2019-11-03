package com.electronicssales.services;

import java.util.List;

import com.electronicssales.models.dtos.TransactionDto;

public interface TransactionService {

    TransactionDto create(TransactionDto transaction);

    List<TransactionDto> findByCustomerInfoId(long customerInfoId);
}