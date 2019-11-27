package com.electronicssales.repositories;

import java.util.List;

import com.electronicssales.entities.TransactionDetailed;
import com.electronicssales.errors.ExceedTheNumberOfProductsException;
import com.electronicssales.models.TransactionDetailedProjections;

public interface CustomizeTransactionDetailedRepository {

    TransactionDetailed saveTransactionDetailed(TransactionDetailed transactionDetailed) throws ExceedTheNumberOfProductsException;

    List<TransactionDetailedProjections> findByTransactionId(long transactionId);
    
}