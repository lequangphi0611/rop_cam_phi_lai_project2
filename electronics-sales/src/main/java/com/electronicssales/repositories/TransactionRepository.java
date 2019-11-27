package com.electronicssales.repositories;

import com.electronicssales.entities.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository 
    extends JpaRepository<Transaction, Long>,
        CustomizeTransactionRepository {

    
}