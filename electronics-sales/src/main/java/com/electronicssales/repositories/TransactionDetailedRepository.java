package com.electronicssales.repositories;

import com.electronicssales.entities.TransactionDetailed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDetailedRepository
        extends JpaRepository<TransactionDetailed, Long>, CustomizeTransactionDetailedRepository {

}