package com.electronicssales.repositories;

import com.electronicssales.entities.TransactionDetailed;
import com.electronicssales.errors.ExceedTheNumberOfProductsException;

public interface CustomizeTransactionDetailedRepository {

    TransactionDetailed saveTransactionDetailed(TransactionDetailed transactionDetailed) throws ExceedTheNumberOfProductsException;
    
}